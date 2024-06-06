package components

import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.vector.ImageVector
import compose.icons.EvaIcons
import compose.icons.evaicons.Fill
import compose.icons.evaicons.Outline
import compose.icons.evaicons.fill.Archive
import compose.icons.evaicons.fill.Bulb
import compose.icons.evaicons.fill.Grid
import compose.icons.evaicons.fill.MessageCircle
import compose.icons.evaicons.outline.Archive
import compose.icons.evaicons.outline.Bulb
import compose.icons.evaicons.outline.Grid
import compose.icons.evaicons.outline.MessageCircle
import screens.HomeView
import screens.ProfileScreen
import screens.TempView
import screens.experimental.QuickTestView

enum class RegisterTabScreen {
    HOME_SCREEN {
        override val imageVector: TabVectorGroup
            get() = TabVectorGroup(EvaIcons.Outline.Grid, EvaIcons.Fill.Grid)

        @Composable
        override fun target() {
            HomeView()
        }
    },
    COMMUNITY_SCREEN {
        override val imageVector: TabVectorGroup
            get() = TabVectorGroup(EvaIcons.Outline.Bulb, EvaIcons.Fill.Bulb)

        @Composable
        override fun target() {
            QuickTestView()
        }
    },
    CHAT_SCREEN {
        override val imageVector: TabVectorGroup
            get() = TabVectorGroup(EvaIcons.Outline.MessageCircle, EvaIcons.Fill.MessageCircle)

        @Composable
        override fun target() {
            TempView(description, imageVector.filled)
        }
    },
    PROFILE_SCREEN {
        override val imageVector: TabVectorGroup
            get() = TabVectorGroup(EvaIcons.Outline.Archive, EvaIcons.Fill.Archive)

        @Composable
        override fun target() {
            ProfileScreen()
        }
    };

    // Properties ⬇️
    val description: String = this.name.replace("_", " ").lowercase()

    // Abstract properties ⬇️
    abstract val imageVector: TabVectorGroup

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

data class TabVectorGroup(val outline: ImageVector, val filled: ImageVector)