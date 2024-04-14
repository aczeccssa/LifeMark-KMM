package screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import cafe.adriel.voyager.core.screen.Screen
import composes.NavigationHeader
import org.jetbrains.compose.ui.tooling.preview.Preview

object InfoScreen : Screen {
    @Composable
    @Preview
    override fun Content() {
        Box {
            NavigationHeader("About LifeMark 2024")
            
            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxSize()
            ) {
                Text("Information about LifeMark-2024")
            }
        }
    }
}