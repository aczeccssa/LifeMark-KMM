package components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AccountCircle
import androidx.compose.material.icons.rounded.DateRange
import androidx.compose.material.icons.rounded.Home
import androidx.compose.material.icons.rounded.Notifications
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import data.units.TimerTest
import screens.HomeView
import screens.TempView
import screens.experimental.SQLExperimentalTest

enum class RegisterTabScreen {
    HOME_SCREEN {
        override val imageVector: ImageVector = Icons.Rounded.Home

        @Composable
        override fun target() {
            HomeView()
        }
    },
    COMMUNITY_SCREEN {
        override val imageVector: ImageVector = Icons.Rounded.DateRange

        @Composable
        override fun target() {
            TimerTest()
        }
    },
    CHAT_SCREEN {
        override val imageVector: ImageVector = Icons.Rounded.Notifications

        @Composable
        override fun target() {
            SQLExperimentalTest()
        }
    },
    PROFILE_SCREEN {
        override val imageVector: ImageVector = Icons.Rounded.AccountCircle

        @Composable
        override fun target() {
            TempView(description, imageVector)
        }
    };

    // Properties ⬇️
    val description: String = this.name.replace("_", " ").lowercase()

    // Abstract properties ⬇️
    abstract val imageVector: ImageVector

    // Abstract method ⬇️
    @Composable
    abstract fun target()
}