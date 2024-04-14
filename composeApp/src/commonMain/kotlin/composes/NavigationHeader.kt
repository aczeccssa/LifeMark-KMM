package composes

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.KeyboardArrowLeft
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow

@Composable
fun NavigationHeader(
    title: String,
    trailing: (@Composable () -> Unit)? = null
) {
    val navigator = LocalNavigator.currentOrThrow
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                // .shadow(elevation = 12.dp, spotColor = Color.LightGray)
                .fillMaxWidth()
                .background(Color.White)
                .safeContentPadding()
                .padding(bottom = 8.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Rounded.KeyboardArrowLeft,
                    contentDescription = "Pop back",
                    modifier = Modifier
                        .size(32.dp).clickable(
                            onClick = { navigator.pop() }, // Navigate back pop.
                            indication = null,
                            interactionSource = remember { MutableInteractionSource() }
                        )
                )
    
                Text(
                    text = title,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 20.sp
                )
            }
    
            if (trailing !== null) trailing() else Spacer(Modifier.width(1.dp))
        }
    }
}