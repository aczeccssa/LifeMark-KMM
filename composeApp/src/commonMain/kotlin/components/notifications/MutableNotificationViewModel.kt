package components.notifications

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import data.models.MutableNotificationData
import data.units.now
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.datetime.LocalDateTime
import viewmodel.NotificationViewModel
import kotlin.math.absoluteValue

class MutableNotificationViewModel(
    private val source: MutableNotificationData
): ViewModel() {
    val expanded: MutableState<Boolean> = mutableStateOf(false)
    val isOpened: MutableState<Boolean> = mutableStateOf(false)
    private val containerDragOffset: MutableState<Float> = mutableStateOf(minNotificationContainerSize.value)

    fun destroySelf() {
        viewModelScope.launch {
            delay(NotificationViewModel.ANIMATION_DURATION)
            NotificationViewModel.destroyNotification(source)
        }
    }

    fun containerClickAction() {
        viewModelScope.launch {
            source.onClick {
                expanded.value = false
                destroySelf()
            }
        }
    }

    fun dragOffsetProcessor(newValue: Float) {
        // FIXME: Not allow change when drag up but closed or drag down but open
        if (newValue.absoluteValue > 0) {
            containerDragOffset.value = (containerDragOffset.value + newValue).coerceIn(
                minNotificationContainerSize.value, maxNotificationContainerSize.value
            )
            if (!expanded.value && isOpened.value && containerDragOffset.value < minNotificationContainerSize.value) {
                this.destroySelf()
                return
            }
        } else return

        println("${LocalDateTime.now()} - Current drag offset: $newValue; Calculated: ${containerDragOffset.value}")
        // FIXME: Drag up or down to analyze is close or open
        // MARK:  [Under parameter A] - Under line the `LaunchedEffect` which observe for state `contentOffset`
        if (containerDragOffset.value > dragSwitchFolderOffsetThreshold) {
            expanded.value = !expanded.value
            if (expanded.value && !isOpened.value) {
                isOpened.value = true
                println("${LocalDateTime.now()} - Update open signal to true")
                NotificationViewModel.madeNotificationPermanently(source)
            }
        }

        // MARK: Reset drag offset.
        if (expanded.value) containerDragOffset.value = minNotificationContainerSize.value
    }
}