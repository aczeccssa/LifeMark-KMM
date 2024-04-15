package screens

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import composes.NavigationHeader
import composes.SnapAlert
import org.jetbrains.compose.ui.tooling.preview.Preview

object InfoScreen : Screen {
    @Composable
    @Preview
    override fun Content() {
        val isSnackBarVisiable = remember { mutableStateOf(false) }
        val snackBarOffsetAnimate = animateDpAsState(
            targetValue = if (isSnackBarVisiable.value) 0.dp else 100.dp,
            animationSpec = tween(durationMillis = 400),
            label = "SnackBar offset transition"
        )

        // On appear show snack bar
        LaunchedEffect(Unit) {
            isSnackBarVisiable.value = true
        }

        // Compose
        Surface {
            NavigationHeader("About LifeMark 2024")

            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxSize().background(MaterialTheme.colors.background)
            ) {
                Text("Information about LifeMark-2024")
            }

            Column(
                verticalArrangement = Arrangement.Bottom,
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxSize().safeContentPadding()
            ) {
                SnapAlert(
                    message = "LifeMark 2024 Dev version 0.1.0",
                    modifier = Modifier.offset(y = snackBarOffsetAnimate.value)
                ) {
                    Button(onClick = { isSnackBarVisiable.value = false }) { Text("Res") }
                }
            }
        }
    }
}