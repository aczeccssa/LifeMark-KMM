package screens.experimental

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.items
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Refresh
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.benasher44.uuid.Uuid
import com.benasher44.uuid.uuid4
import components.LazyColumnRoundedContainer
import data.ProgressDispatchStateStruct
import data.models.RocketLaunch
import data.modules.getViewModel
import data.sdk.SpaceXSDK
import data.units.now
import io.github.aakira.napier.Napier
import kotlinx.coroutines.launch
import kotlinx.datetime.LocalDateTime
import viewmodel.NotificationViewModel
import viewmodel.SnapAlertViewModel

@Composable
fun SpaceXLauncherHistory(
    /** View model about SpaceX rocket launchers. */
//    viewModel: SpaceXLauncherHistoryViewModel = viewModel {
//        SpaceXLauncherHistoryViewModel(
//            SpaceXSDK(databaseDriverFactory = DatabaseDriverFactory(), SpaceXApi())
//        )
//    }
) {
    val viewModel: SpaceXLauncherHistoryViewModel = getViewModel()
    // State about loading status and launches list.
    val state by remember { viewModel.state }

    // Views...
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth().padding(vertical = 6.dp)
    ) {
        Text("SpaceX", style = MaterialTheme.typography.h6)

        Icon(
            Icons.Rounded.Refresh,
            contentDescription = null,
            tint = MaterialTheme.colors.primary,
            modifier = Modifier.clickable { viewModel.loadLaunches() }.size(28.dp)
        )
    }

    if (state.isLoading) {
        Row(Modifier.fillMaxSize(), Arrangement.Center, Alignment.CenterVertically) {
            Text("Loading...", style = MaterialTheme.typography.h5)
            CircularProgressIndicator(Modifier.size(32.dp))
        }
    } else {
        Spacer(Modifier.height(12.dp))
        LazyColumnRoundedContainer {
            items(state.data.reversed()) { launch: RocketLaunch ->
                RocketLaunchItem(launch)
                Divider()
            }
        }
    }
}

@Composable
private fun RocketLaunchItem(launch: RocketLaunch) {
    // Properties
    val appThemeSuccessful = Color(0xff4BB543)
    val appThemeUnsuccessful = Color(0xffFC100D)

    Column(modifier = Modifier.padding(all = 16.dp)) {
        Text(
            text = "${launch.missionName} - ${launch.launchYear}",
            style = MaterialTheme.typography.subtitle1
        )
        Spacer(Modifier.height(8.dp))
        Text(
            text = if (launch.launchSuccess == true) "Successful" else "Unsuccessful",
            color = if (launch.launchSuccess == true) appThemeSuccessful else appThemeUnsuccessful,
            style = MaterialTheme.typography.body2
        )
        Spacer(Modifier.height(8.dp))
        val details = launch.details
        if (details?.isNotBlank() == true) Text(details, style = MaterialTheme.typography.body2)
    }
}

/**
 * SQLite Digital experimental viewmodel.
 *
 * @property state [MutableState] of [RocketLaunchScreenState] The binding state value.
 */
class SpaceXLauncherHistoryViewModel(private val sdk: SpaceXSDK, val id: Uuid = uuid4()) : ViewModel() {
    companion object {
        private const val TAG = "SpaceXLauncherHistoryViewModel"
    }

    init {
        Napier.i("${LocalDateTime.now()} - SQL experimental view model online: $id", tag = TAG)
    }

    override fun onCleared() {
        Napier.i("${LocalDateTime.now()} - Notification view model offline: $id", tag = TAG)
        super.onCleared()
    }

    private var _state = mutableStateOf(RocketLaunchScreenState())
    val state: MutableState<RocketLaunchScreenState> = this._state

    init {
        // Load data.
        loadLaunches()
    }

    /** Refresh rocket launchers. */
    fun loadLaunches() {
        viewModelScope.launch {
            _state.value = _state.value.copy(isLoading = true, data = emptyList())
            try {
                val launches = sdk.getLaunches(forceReload = false)
                _state.value = _state.value.copy(isLoading = false, data = launches)
            } catch (e: Exception) {
                NotificationViewModel.pushNotification(e) { loadLaunches() }
                _state.value = _state.value.copy(isLoading = false, data = emptyList())
            }
            SnapAlertViewModel.pushSnapAlert("Launches loaded🌟")
        }
    }
}

/**
 * Rocket Launch Screen State
 *
 * @property isLoading [Boolean] is current data loading.
 * @property data [List] of [RocketLaunch] launch rocket list.
 */
data class RocketLaunchScreenState(
    override val isLoading: Boolean = false, override val data: List<RocketLaunch> = emptyList()
) : ProgressDispatchStateStruct<List<RocketLaunch>>