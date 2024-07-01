package components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import compose.icons.EvaIcons
import compose.icons.evaicons.Fill
import compose.icons.evaicons.Outline
import compose.icons.evaicons.fill.Attach
import compose.icons.evaicons.fill.BarChart
import compose.icons.evaicons.fill.Browser
import compose.icons.evaicons.fill.MessageSquare
import compose.icons.evaicons.outline.Attach
import compose.icons.evaicons.outline.BarChart
import compose.icons.evaicons.outline.Browser
import compose.icons.evaicons.outline.MessageSquare
import screens.mainscreens.HomeView
import screens.mainscreens.ProfileScreen
import screens.mainscreens.QuickTestView

enum class RegisterTabScreen {
    HOME_SCREEN {
        override val imageVector: TabVectorGroup
            get() = TabVectorGroup(EvaIcons.Outline.BarChart, EvaIcons.Fill.BarChart)

        @Composable
        override fun target() = HomeView()
    },
    COMMUNITY_SCREEN {
        override val imageVector: TabVectorGroup
            get() = TabVectorGroup(EvaIcons.Outline.Attach, EvaIcons.Fill.Attach)

        @Composable
        override fun target() = QuickTestView()
    },
    CHAT_SCREEN {
        override val imageVector: TabVectorGroup
            get() = TabVectorGroup(EvaIcons.Outline.MessageSquare, EvaIcons.Fill.MessageSquare)

        @Composable
        override fun target() {
            Column(
                modifier = Modifier.fillMaxSize().background(MaterialTheme.colors.background),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Temp icon
                Icon(imageVector.filled, name, Modifier.size(64.dp), MaterialTheme.colors.primary)
                Text(description, fontSize = 18.sp, fontWeight = FontWeight.SemiBold)
            }
        }
    },
    PROFILE_SCREEN {
        override val imageVector: TabVectorGroup
            get() = TabVectorGroup(EvaIcons.Outline.Browser, EvaIcons.Fill.Browser)

        @Composable
        override fun target() = ProfileScreen()
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
        val contentScreenPrinter get() = this._contentScreenPrinter

        /**
         * Set the current pointer of the navigator.
         *
         * @param newValue [RegisterTabScreen] The new pointer.
         */
        fun setContentScreenPrinter(newValue: RegisterTabScreen) {
            _contentScreenPrinter.value = newValue
        }
    }
}

data class TabVectorGroup(val outline: ImageVector, val filled: ImageVector)