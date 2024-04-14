package screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import io.kamel.image.KamelImage
import io.kamel.image.asyncPainterResource
import org.jetbrains.compose.ui.tooling.preview.Preview

// Anyway if `object`, `class` even `data class` are able to inherit from `Screen`.
object HomeScreen : Screen {
    @Composable
    @Preview
    override fun Content() {
        // `LocalNavigator.currentOrThrow` only available in `@Composable`. 
        val navigator = LocalNavigator.currentOrThrow

        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxSize()
        ) {
            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.clickable(
                    onClick = {
                        navigator.push(InfoScreen) // Navigate to new screen.
                    },
                    indication = null,
                    interactionSource = remember { MutableInteractionSource() }
                )
            ) {
                val kotlinSqureCover = "https://www.runoob.com/wp-content/uploads/2017/05/kotlin_250x250.png"
                KamelImage(
                    resource = asyncPainterResource(data = kotlinSqureCover),
                    contentDescription = "org.kotlinlang.chunks.hero-cover",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.size(180.dp)
                )

                Text(
                    text = "LIFEMARK-2024",
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF535BF2)
                )

                Text("kotlin multiplatform", fontWeight = FontWeight.Light)
            }
        }
    }
}
