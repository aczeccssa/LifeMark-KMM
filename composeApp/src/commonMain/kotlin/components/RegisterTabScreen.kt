package components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AccountCircle
import androidx.compose.material.icons.rounded.DateRange
import androidx.compose.material.icons.rounded.Home
import androidx.compose.material.icons.rounded.Notifications
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.vector.ImageVector
import screens.HomeView
import screens.TempView
import screens.experimental.TrackTimerTest

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
            TrackTimerTest()
        }
    },
    CHAT_SCREEN {
        override val imageVector: ImageVector = Icons.Rounded.Notifications

        @Composable
        override fun target() {
            TempView(description, imageVector)
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

    companion object {
        private val _contentScreenPrinter = mutableStateOf(HOME_SCREEN)
        val contentScreenPrinter: MutableState<RegisterTabScreen>
            get() = this._contentScreenPrinter

        /**
         * Set the current pointer of the navigator.
         *
         * @param newValue [RegisterTabScreen] The new pointer.
         */
        fun setContentScreenPrinter(newValue: RegisterTabScreen) {
            this._contentScreenPrinter.value = newValue
        }
    }
}