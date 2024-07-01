package screens.experimental

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

@Composable
private fun StateView(index: Int) {
    Box(Modifier.fillMaxSize(), Alignment.Center) {
        Text("Index: $index")
    }
}