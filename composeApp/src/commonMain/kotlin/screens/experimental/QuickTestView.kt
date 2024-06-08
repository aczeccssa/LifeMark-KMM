package screens.experimental

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Tab
import androidx.compose.material.Text
import androidx.compose.material3.DatePicker
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.PrimaryTabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import components.ColorAssets
import components.ColumnRoundedContainer
import components.LargeButton
import components.navigator.MainNavigator
import components.properties
import compose.icons.EvaIcons
import compose.icons.evaicons.Outline
import compose.icons.evaicons.outline.Clock
import compose.icons.evaicons.outline.Save
import data.SpecificConfiguration
import data.platform.LocalPreferences
import data.units.TrackTimer
import data.units.fromEpochMilliseconds
import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDateTime
import viewmodel.NotificationViewModel
import viewmodel.SnapAlertViewModel

private enum class QuickTestInitiates {
    TRACK_TIMER {
        override val imageVector: ImageVector = EvaIcons.Outline.Clock

        @Composable
        override fun target() {
            TimerTest()
        }
    },
    USER_PREFERENCES {
        override val imageVector: ImageVector = EvaIcons.Outline.Save

        @Composable
        override fun target() {
            UserPreferencesTest()
        }
    };

    val title: String
        get() {
            val contentList = mutableListOf<String>()
            // name.replace("_", " ").lowercase()
            name.split("_").forEach { content ->
                val firstChar = content.first().uppercase()
                contentList.add(firstChar + content.drop(1).lowercase())
            }
            return contentList.joinToString(" ")
        }

    @Composable
    abstract fun target()

    abstract val imageVector: ImageVector
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QuickTestView() {
    val scrollState = rememberScrollState()
    var state by remember { mutableStateOf(QuickTestInitiates.TRACK_TIMER) }

    Column {
        MainNavigator("Quick Test")

        PrimaryTabRow(selectedTabIndex = state.ordinal,
            containerColor = MaterialTheme.colors.background,
            contentColor = MaterialTheme.colors.onBackground,
            indicator = {
                TabRowDefaults.PrimaryIndicator(
                    modifier = Modifier.tabIndicatorOffset(
                        state.ordinal, matchContentSize = true
                    ), width = Dp.Unspecified, color = MaterialTheme.colors.primary
                )
            },
            divider = { HorizontalDivider(color = ColorAssets.LightGray.value) }) {
            QuickTestInitiates.entries.forEach { item ->
                val iconTint = animateColorAsState(
                    targetValue = if (state === item) MaterialTheme.colors.primary else MaterialTheme.colors.onBackground,
                    animationSpec = tween(MaterialTheme.properties.defaultAnimationDuration.toInt())
                )
                Tab(selected = state === item,
                    icon = {
                        Icon(item.imageVector, item.name, Modifier.size(20.dp), iconTint.value)
                    },
                    onClick = { state = item },
                    text = {
                        Text(
                            text = item.title,
                            maxLines = 2,
                            overflow = TextOverflow.Ellipsis,
                            letterSpacing = 0.sp,
                            style = MaterialTheme.typography.body2
                        )
                    },
                    interactionSource = MutableInteractionSource(),
                    selectedContentColor = MaterialTheme.colors.primary,
                    unselectedContentColor = MaterialTheme.colors.onBackground
                )
            }
        }

        Column(
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.verticalScroll(scrollState).fillMaxWidth()
                .background(MaterialTheme.colors.background)
                .padding(SpecificConfiguration.defaultContentPadding)
        ) { state.target() }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TimerTest() {
    var diff: Int? by remember { mutableStateOf(null) }
    var expectedDuration by remember { mutableStateOf(10000) }
    var expired by remember { mutableStateOf(false) }
    val timer: TrackTimer by remember {
        mutableStateOf(TrackTimer.create(expectedDuration.toLong()) {
            diff = it.duration.toInt() // Differences.
            expired = diff!!.toLong() > expectedDuration // Is expired?
        })
    }

    val timerRunningState by remember { timer.isRunning }

    val datePickerState = rememberDatePickerState(
        initialSelectedDateMillis = Clock.System.now().toEpochMilliseconds()
    )

    DisposableEffect(Unit) {
        onDispose {
            // Close timer when this scope is disposed.
            if (timerRunningState) {
                timer.dispose()
                // Remind user timer end and the result.
                SnapAlertViewModel.pushSnapAlert("Timer duration: ${diff.toString()}ms")
            }
            // Clear timer.
            timer.reset()
        }
    }

    fun timerRunHandle() {
        if (timerRunningState) {
            try {
                timer.dispose()
            } catch (e: Throwable) {
                NotificationViewModel.pushNotification(e)
            }
        } else {
            timer.reset()
            timer.enableDefaultLogoutAction = true
            timer.launch()
        }
    }

    // Views
    ColumnRoundedContainer {
        DatePicker(state = datePickerState,
            colors = MaterialTheme.properties.defaultDatePickerColors,
            title = {
                datePickerState.selectedDateMillis?.let {
                    Text("Selected date: ${LocalDateTime.fromEpochMilliseconds(it)}")
                } ?: Text("Nothing been selected.")
            })
    }

    Spacer(Modifier.height(24.dp))

    ColumnRoundedContainer(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text(text = if (timerRunningState) "Timer running..." else {
            diff?.let { "Duration: ${diff.toString()}ms" } ?: "Click start to launch timer"
        }, color = if (expired) Color.Red else Color.Green)

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier.clip(RoundedCornerShape(24.dp)).fillMaxWidth()
                .border(2.dp, MaterialTheme.colors.primary, RoundedCornerShape(24.dp))
                .padding(24.dp, 8.dp)
        ) {
            BasicTextField(
                expectedDuration.toString(),
                onValueChange = { expectedDuration = it.toInt(10000) },
                modifier = Modifier.fillMaxWidth(),
                textStyle = MaterialTheme.typography.subtitle1.copy(color = MaterialTheme.colors.primary),
            )
        }

        LargeButton(
            text = "${if (timerRunningState) "Stop" else "Start"} Timer",
            modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)
        ) { timerRunHandle() }
    }
}

private const val TEST_SAVED_VALUE = "刘伟斌小小丑"
private const val FAILED_EXPR = "Null value means not saved."
private const val TEST_KEY = "test_value"

@Composable
private fun UserPreferencesTest() {
    var getValue: String? by remember { mutableStateOf(null) }

    LaunchedEffect(Unit) {
        getValue = LocalPreferences.getString(TEST_KEY, FAILED_EXPR)
    }

    ColumnRoundedContainer(Arrangement.spacedBy(12.dp)) {
        Box(
            Modifier.fillMaxWidth().height(120.dp).clip(RoundedCornerShape(12.dp))
                .background(ColorAssets.LightGray.value), Alignment.Center
        ) {
            Text(getValue ?: "Empty value.", style = MaterialTheme.typography.subtitle1)
        }

        LargeButton("Set value", RoundedCornerShape(12.dp)) {
            LocalPreferences.putString(TEST_KEY, TEST_SAVED_VALUE)
        }

        LargeButton("Get value", RoundedCornerShape(12.dp)) {
            getValue = LocalPreferences.getString(TEST_KEY, FAILED_EXPR)
        }

        LargeButton("Clean value", RoundedCornerShape(12.dp)) {
            LocalPreferences.remove(TEST_KEY)
        }
    }
}

