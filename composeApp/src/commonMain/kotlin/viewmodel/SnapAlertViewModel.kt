package viewmodel

import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import components.properties
import data.models.SnapAlertData
import data.units.CST
import data.units.now
import io.github.aakira.napier.Napier
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toInstant

object SnapAlertViewModel {
    // Static configurations
    val ANIMATION_DURATION = MaterialTheme.properties.defaultAnimationDuration
    const val LIFE_CYCLE_TIMEOUT = 10000L
    private const val CHECK_INTERVAL = 500L
    private val TOTAL_DURATION = LIFE_CYCLE_TIMEOUT + 2 * ANIMATION_DURATION
    private const val MAX_QUEUE_SIZE = 5

    const val TAG = "SnapAlertViewModel"

    // Launch the processor coroutine
    init {
        Napier.i("${LocalDateTime.now()} - Snap alert view model online(Global view model)", tag = TAG)
    }

    // Data queue
    private var _queue: MutableList<SnapAlertData> = mutableStateListOf()
    val queue: MutableList<SnapAlertData> get() = this._queue
    // Add Mutex for queue
    private val mainQueueMutex = Mutex()

    private val tempQueue: MutableList<SnapAlertData> = mutableStateListOf()

    private val _currentMainScreen: MutableState<Boolean> = mutableStateOf(true)
    val currentMainScreen: MutableState<Boolean> get() = _currentMainScreen

    fun updateScreenState(state: Boolean) {
        _currentMainScreen.value = state
    }

    private var isProcessorLaunched = false

    // Coroutine Scope for this view model only.
    private val viewModelScope = CoroutineScope(SupervisorJob() + Dispatchers.Main.immediate)

    // Flow observer to track the queue length
    private val queueLengthFlow = flow {
        while (true) {
            emit(_queue.size)
            delay(CHECK_INTERVAL) // Check in 500ms
        }
    }.distinctUntilChanged() // Make sure the value is distinct from the previous one

    /**
     * Push a snap alert to the queue.
     *
     * This push will start a coroutine that will push the snap alert to the queue, only push to queue
     * when queue number less then 5.
     *
     * @param message [String] The data of the snap alert.
     */
    fun pushSnapAlert(message: String) {
        this.tempQueue.add(SnapAlertData(message))

        if (!isProcessorLaunched) launchProcessor()
    }

    private fun launchProcessor() {
        viewModelScope.launch {
            pushSnapToQueueProcessor()
        }
    }

    /**
     * Push a snap alert to the queue.
     *
     * @author Lester E
     */
    private suspend fun pushSnapToQueueProcessor() {
        queueLengthFlow.collect { currentSize ->
            destroySnapAlertProcessor()
            if (currentSize < MAX_QUEUE_SIZE) {
                val data = tempQueue.firstOrNull()
                data?.also {
                    tempQueue.remove(it)
                    it.launchTime = LocalDateTime.now()
                    _queue.add(it)
                    Napier.i("${LocalDateTime.now()} - Snap alert pushed to queue: ${it.id}", tag = TAG)
                }
            }
        }
    }

    private fun destroySnapAlertProcessor() {
        _queue.removeAll {
            it.launchTime?.let { launchTime ->
                val diff = Clock.System.now().minus(launchTime.toInstant(TimeZone.CST)).inWholeMilliseconds
                return@removeAll diff >= TOTAL_DURATION
            }
            return@removeAll false
        }
    }

    /**
     * Destroy a snap alert from the queue.
     *
     * @param instance [SnapAlertData] The data instance of the snap alert.
     *
     * @author Lester E
     */
    fun destroySnapAlert(instance: SnapAlertData) {
        viewModelScope.launch {
            delay(ANIMATION_DURATION)
            mainQueueMutex.withLock {
                _queue.removeAll { it == instance }
                if (_queue.all { it == instance }) {
                    Napier.e("${LocalDateTime.now()} - Destroy failed: ${instance.id}", tag = TAG)
                    return@launch
                }
                Napier.i("${LocalDateTime.now()} - Snap alert destroyed: ${instance.id}, queue size: ${_queue.size}", tag = TAG)
            }
        }
    }
}