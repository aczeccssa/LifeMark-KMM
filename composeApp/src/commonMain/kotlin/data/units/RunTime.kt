package data.units

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.CheckCircle
import androidx.compose.material.icons.rounded.ThumbUp
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.benasher44.uuid.Uuid
import com.benasher44.uuid.uuid4
import components.LargeButton
import components.Rectangle
import components.RegisterTabScreen
import components.navigator.MainNavigator
import kotlinx.coroutines.delay
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import screens.InfoScreen
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
class CodableException(code: Int, override val message: String) : Exception(message)

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

    /** This timer already expired. */
    TIMER_EXPIRED(
        CodableException(1004, "Timer already expired.")
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
 *     println("Timer started!")
 * }) { scope ->
 *     println("Timer running duration: ${scope.end.minus(scope.start).inWholeMilliseconds} ms")
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

    private var _expired: Boolean = false
    val expired: Boolean get() = this._expired

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
    }

    /** Default disposed action. */
    private fun defaultDisposedLog() {
        this.scope?.let { scope ->
            if (scope.duration != expectedDuration) {
                val moment = if (scope.duration < expectedDuration) '-' else '+'
                val offset = scope.duration - expectedDuration
                val outOfTime = Error("Timer running duration unexpected: ${moment}${offset}ms.")
                println(outOfTime)
            } else {
                println("Timer finished in ${scope.duration}ms.")
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
        else if (launchedMode === TrackTimerMode.PROXY)
            throw TrackTimerExceptions.PROXY_LAUNCHED_ONLY.exception

        // Start timer...
        this.startTime = Clock.System.now()
        this.launchScope()
        // Start dialog...
        println("Timer start on ${this.startTime}.")
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
        // Make sure this timer is not expired...
        if (_expired) throw TrackTimerExceptions.TIMER_EXPIRED.exception

        // Only can dispose when this timer has been launched.
        if (!launched) throw TrackTimerExceptions.TIMER_OFFLINE.exception

        // Update timer mode.
        launchedMode = TrackTimerMode.PROXY

        // Set end time.
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

        // Signal timer already expired.
        _expired = true
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
        this.scope = null
        this.startTime = DEFAULT_TIME
        this.endTime = DEFAULT_TIME
        this.enableDefaultLogoutAction = true
        this.launchedMode = TrackTimerMode.NORMAL
        this._expired = false
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

@Composable
fun TimerTest() {
    var diff: Int? by remember { mutableStateOf(null) }
    var expectedDuration by remember { mutableStateOf(10000) }
    var expired by remember { mutableStateOf(false) }
    val timer: TrackTimer by remember { mutableStateOf(
        TrackTimer.create(expectedDuration.toLong()) {
            diff = it.duration.toInt() // Differences.
            expired = diff!!.toLong() > expectedDuration // Is expired?
        }.launch()
    ) }

    Column {
        MainNavigator(Icons.Rounded.ThumbUp, "Track Timer(Dev)")

        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxSize()
        ) {
            diff?.let {
                Text(
                    text = "Duration: ${diff.toString()}ms",
                    color = if (expired) Color.Red else Color.Green
                )
            }

            Spacer(Modifier.height(12.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier.clip(RoundedCornerShape(24.dp)).fillMaxWidth(0.7f)
                    .border(2.dp, MaterialTheme.colors.primary, RoundedCornerShape(24.dp))
                    .padding(24.dp, 18.dp)
            ) {
                BasicTextField(
                    expectedDuration.toString(),
                    onValueChange = { expectedDuration = it.toInt(10000) },
                    modifier = Modifier.fillMaxWidth(),
                    textStyle = TextStyle(color = MaterialTheme.colors.onSurface),
                )
            }

            Spacer(Modifier.height(12.dp))

            LargeButton(
                text = "${if (timer.expired) "Start" else "Stop"} Timer",
                modifier = Modifier.fillMaxWidth(0.7f).padding(vertical = 8.dp)
            ) {
                if (timer.expired) {
                    timer.reset()
                    timer.enableDefaultLogoutAction = true
                } else {
                    timer.dispose()
                }
            }

            val navigator = LocalNavigator.currentOrThrow

            LargeButton(
                text = "To new screen",
                modifier = Modifier.fillMaxWidth(0.7f).padding(vertical = 8.dp)
            ) {
                navigator.push(InfoScreen)
            }
        }
    }
}