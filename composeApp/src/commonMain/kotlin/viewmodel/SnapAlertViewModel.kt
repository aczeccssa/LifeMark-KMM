package viewmodel

import data.models.SnapAlertData
import data.units.TrackTimer
import data.units.now
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import kotlinx.datetime.LocalDateTime

object SnapAlertViewModel {
    // Data queue
    val queue: MutableList<SnapAlertData> = mutableListOf()

    // Static configurations
    const val LIFE_CYCLE_TIMEOUT = 10000L
    const val ANIMATION_DURATION = 600L
    private const val MAX_QUEUE_SIZE = 5
    private const val TOTAL_DURATION = LIFE_CYCLE_TIMEOUT + 2 * ANIMATION_DURATION

    // Flow observer to track the queue length
    private val queueLengthFlow = flow {
        while (true) {
            emit(queue.size)
            delay(500) // Check in 500ms
        }
    }.distinctUntilChanged() // Make sure the value is distinct from the previous one

    // Coroutine Scope for this view model only.
    private val coroutineScope = CoroutineScope(Dispatchers.IO)

    /**
     * Push a snap alert to the queue.
     *
     * This push will start a coroutine that will push the snap alert to the queue, only push to queue
     * when queue number less then 5.
     *
     * @param snapAlertData [SnapAlertData] The data of the snap alert.
     */
    fun pushSnapAlert(snapAlertData: SnapAlertData) {
        coroutineScope.launch {
            pushSnapToQueueProcessor(snapAlertData)
        }
    }

    /**
     * Push a snap alert to the queue.
     *
     * @param snapAlertData [SnapAlertData] The data of the snap alert.
     *
     * @author Lester E
     */
    private suspend fun pushSnapToQueueProcessor(snapAlertData: SnapAlertData) {
        val timer = TrackTimer.create(TOTAL_DURATION) { }.launch()
        queueLengthFlow.collect { newValue ->
            if (newValue < MAX_QUEUE_SIZE) {
                queue.add(snapAlertData)
                timer.dispose()
                println("${LocalDateTime.now()} - Snap alert pushed to queue: ${snapAlertData.id}")
            }
        }
    }

    /**
     * Destroy a snap alert from the queue.
     *
     * @param instance [SnapAlertData] The data of the snap alert.
     *
     * @author Lester E
     */
    suspend fun destroySnapAlert(instance: SnapAlertData) {
        delay(ANIMATION_DURATION)
        queue.remove(instance)
        println("${LocalDateTime.now()} - Snap alert destroyed: ${instance.id}")
    }
}