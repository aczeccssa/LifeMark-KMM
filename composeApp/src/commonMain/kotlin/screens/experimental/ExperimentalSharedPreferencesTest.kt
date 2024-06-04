package screens.experimental

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import components.ColumnRoundedContainer
import components.LargeButton
import components.navigator.MainNavigator
import data.SpecificConfiguration
import data.platform.LocalPreferences


const val TEST_SAVED_VALUE = "刘伟斌小小丑"
const val FAILED_EXPR = "Null value means not saved."
const val TEST_KEY = "test_value"

@Composable
fun ExperimentalSharedPreferencesTest() {
    val scrollState = rememberScrollState()
    var getValue: String? by remember { mutableStateOf(null) }

    Column {
        MainNavigator("Track Timer Test")

        Column(
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.verticalScroll(scrollState).fillMaxWidth()
                .background(MaterialTheme.colors.background)
                .padding(SpecificConfiguration.defaultContentPadding)
        ) {
            ColumnRoundedContainer(Arrangement.spacedBy(12.dp)) {
                Text(getValue ?: "Empty value.")

                LargeButton("Set value", modifier = Modifier.fillMaxWidth()) {
                    LocalPreferences.putString(TEST_KEY, TEST_SAVED_VALUE)
                }

                LargeButton("Update value", modifier = Modifier.fillMaxWidth()) {
                    getValue = LocalPreferences.getString(TEST_KEY, FAILED_EXPR)
                }
            }
        }
    }
}