package data.units

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import com.benasher44.uuid.Uuid
import com.benasher44.uuid.uuid4
import io.github.aakira.napier.Napier
import kotlinx.coroutines.delay
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDateTime
import kotlin.coroutines.cancellation.CancellationException
import kotlin.properties.Delegates

/**
 * The mode of track timer.
 *
 * @author Lester E
 */
enum class TrackTimerMode {
    /** The base mode, enable call launch method and chain callable. */
    NORMAL,

    /** Timer will launched and disposed in an expected time automatically. */
    PROXY;
}

/**
 * The exception of track timer.
 *
 * @property code [Int] The error code.
 * @property message [String] The error message.
 *
 * @author Lester E
 */
class CodableException(code: Int, override val message: String) : Exception(message) {
    companion object {
        val FabricationFunctionalError = CodableException(-901, "Fabrication functional error.")
    }
}

/**
 * Exceptions error of [TrackTimer].
 *
 * @author Lester E
 */
enum class TrackTimerExceptions(val exception: CodableException) {
    /** Launch methods are not available to call double times. */
    DOUBLE_LAUNCHED(
        CodableException(1001, "Unable to run launch action when this timer has been launched.")
    ),

    /** Timer might not be launched. */
    TIMER_OFFLINE(
        CodableException(1002, "Timer has not been launched.")
    ),

    /** Dispose scope still not be initialized. */
    DISPOSE_SCOPE_UNINITIALIZED(
        CodableException(1003, "Scope is not be initialized, call the launch method first.")
    ),

    /** Proxy mode cannot launched with base launch method. */
    PROXY_LAUNCHED_ONLY(
        CodableException(2001, "Proxy mode is not available to use base launch.")
    );
}


/**
 * A timer that can be used to measure the duration of a task.
 *
 * Common call:
 * ```kotlin
 * val timer = SchubertTimer.create(10000L, {
 *     Napier("Timer started!")
 * }) { scope ->
 *     Napier("Timer running duration: ${scope.end.minus(scope.start).inWholeMilliseconds} ms")
 * }
 * timer.launch()
 * ... some time later ...
 *
 * timer.dispose()
 * ```
 *
 * Quick call: (`launch` method can call and back as chain call)
 * ```kotlin
 * val timer = SchubertTimer.create(10000L) {...}.launch()
 * timer.dispose()
 * ```
 *
 * Proxy mode:
 * ```kotlin
 * SchubertTimer.create(10000L) {...}.launchSuspend()
 * ```
 *
 * * Timer mode will automatically change to proxy mode when `launchSuspend` called.
 * * No way to modifier the timer mode by hand.
 *
 * @author Lester E
 */
class TrackTimer private constructor() {
    private var startTime: Instant = DEFAULT_TIME
    private var endTime: Instant = DEFAULT_TIME

    // Disposed timer scope.
    private var scope: SchubertTimerScope? = null

    // Timer identifier
    val id: Uuid = uuid4()

    // Signal
    private var launched: Boolean = false

    private val _runningState: MutableState<Boolean> = mutableStateOf(false)

    /** Signal this timer running state. */
    val isRunning: MutableState<Boolean> get() = this._runningState

    // Current timer mode.
    private var launchedMode: TrackTimerMode = TrackTimerMode.NORMAL

    /** Should enable default logout action when disposed. */
    var enableDefaultLogoutAction: Boolean = true

    companion object {
        /**
         * Create a new timer with a given expected duration.
         *
         * @param expectedDuration [Long] in milliseconds.
         * @param launchScope [Function] the scope to launch.
         * @param disposedScope [Function] the scope to dispose.
         *
         * @author Lester E
         */
        fun create(
            expectedDuration: Long,
            launchScope: () -> Unit = { },
            disposedScope: (scope: SchubertTimerScope) -> Unit
        ): TrackTimer {
            return TrackTimer().apply {
                this.expectedDuration = expectedDuration
                this.launchScope = launchScope
                this.disposedScope = disposedScope
            }
        }

        val DEFAULT_TIME = Clock.ISO_ZERO

        private const val TAG = "TrackTimer"
    }

    /** Default disposed action. */
    private fun defaultDisposedLog() {
        this.scope?.let { scope ->
            if (scope.duration != expectedDuration) {
                val moment = if (scope.duration < expectedDuration) '-' else '+'
                val offset = scope.duration - expectedDuration
                val outOfTime = Error("Timer running duration unexpected: ${moment}${offset}ms.")
                Napier.w("${LocalDateTime.now()} - Timer out of time.", outOfTime, TAG)
            } else {
                Napier.i("Timer finished in ${scope.duration}ms.", tag = TAG)
            }
        }
    }

    /**
     * Handle to launch this timer.
     *
     * @exception [CodableException] Unable to run launch action when this timer has been launched.
     * @exception [CodableException] Proxy mode is not available to use base launch.
     *
     * @author Lester E
     */
    @Throws(CodableException::class)
    fun launch(): TrackTimer {
        if (launched) throw TrackTimerExceptions.DOUBLE_LAUNCHED.exception
        else if (launchedMode === TrackTimerMode.PROXY) throw TrackTimerExceptions.PROXY_LAUNCHED_ONLY.exception

        // Start timer...
        this._runningState.value = true
        this.startTime = Clock.System.now()
        this.launchScope()
        // Start dialog...
        Napier.i("Timer start on ${this.startTime}.", tag = TAG)
        // Update signal
        this.launched = true
        // Enable chain call...
        return this
    }

    /**
     * Handle to finish this timer.
     *
     * @exception [CodableException] Timer already expired.
     * @exception [CodableException] Timer has not been launched.
     * @exception [CodableException] Scope is not be initialized, please check you're call the launch method.
     *
     * @author Lester E
     */
    @Throws(CodableException::class)
    fun dispose() {
        // Only can dispose when this timer has been launched.
        if (!launched) throw TrackTimerExceptions.TIMER_OFFLINE.exception

        // Update timer mode.
        // launchedMode = TrackTimerMode.PROXY

        // Set end time.
        this._runningState.value = false
        endTime = Clock.System.now()
        // Generate and assign scope
        scope = SchubertTimerScope(startTime, endTime)

        // Make sure scope is initialized.
        if (scope === null) throw TrackTimerExceptions.DISPOSE_SCOPE_UNINITIALIZED.exception
        scope?.let { scope ->
            // Make sure scope is not null.
            this.disposedScope(scope)
            if (enableDefaultLogoutAction) this.defaultDisposedLog()
        }
    }

    /**
     * Expected duration launcher, like the `setTimeOut` of `JavaScript`.
     *
     * @param expectedDuration [Long] Duration of this timer, default is this timer's expected duration.
     *
     * @exception [CodableException] Unable to run launch action when this timer has been launched.
     *
     * @author Lester E
     */
    @Throws(CodableException::class, CancellationException::class)
    suspend fun launchSuspend(expectedDuration: Long = this.expectedDuration) {
        if (launched) throw TrackTimerExceptions.DOUBLE_LAUNCHED.exception

        // Start timer...
        val proxy = this.launch()
        // After expected time...
        delay(expectedDuration) // MARK: Using this method's parameter.
        // Start dispose action...
        proxy.dispose()
    }

    /**
     * This method will reset all properties to default, params will not.
     */
    fun reset(): TrackTimer {
        // Unable to reset when this timer has been launched.
        if (_runningState.value) {
            Napier.e(
                message = "${LocalDateTime.now()} - Timer still running: $id",
                throwable = TrackTimerExceptions.DOUBLE_LAUNCHED.exception,
                tag = TAG
            )
            return this
        }
        // Reset all properties.
        this.scope = null
        this.startTime = DEFAULT_TIME
        this.endTime = DEFAULT_TIME
        this.enableDefaultLogoutAction = true
        this.launchedMode = TrackTimerMode.NORMAL
        this.launched = false
        return this
    }

    // Action when start timer.
    private lateinit var launchScope: () -> Unit

    // Action when finished timer.
    private lateinit var disposedScope: (SchubertTimerScope) -> Unit

    // Expected duration!
    private var expectedDuration by Delegates.notNull<Long>()
}

/**
 * Schubert timer disposed scope original from [TrackTimer].
 *
 * @param start [Instant] the start time of this timer.
 * @param end [Instant] the end time of this timer.
 *
 * @author Lester E
 */
data class SchubertTimerScope(val start: Instant, val end: Instant) {
    val duration: Long = end.minus(start).inWholeMilliseconds
}