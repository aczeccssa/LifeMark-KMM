package screens.profiles

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import components.ColumnRoundedContainer
import components.NavigationHeader
import data.NavigationHeaderConfiguration
import data.SpecificConfiguration
import data.resources.LifeMarkIntroduction
import dev.chrisbanes.haze.HazeState
import screens.NAVIGATION_BAR_HEIGHT

object AboutLifeMark : Screen {
    @Composable
    override fun Content() {
        val scrollState = rememberScrollState()
        val topOffset = NavigationHeaderConfiguration.defaultConfiguration.headerHeight + 28.dp
        val hazeState = remember { HazeState() }

        Surface {
            NavigationHeader("About LifeMark2024")
            Column(
                Modifier.verticalScroll(scrollState).fillMaxSize()
                    .background(MaterialTheme.colors.background)
                    .padding(SpecificConfiguration.defaultContentPadding)
                    .padding(top = topOffset + SpecificConfiguration.defaultContentPadding)
                    .padding(bottom = NAVIGATION_BAR_HEIGHT),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text(
                    "LifeMarK 2024 - ${LifeMarkIntroduction.SLOGAN}",
                    style = MaterialTheme.typography.h6,
                    fontWeight = FontWeight.SemiBold
                )

                ColumnRoundedContainer {
                    Text(
                        text = LifeMarkIntroduction.RESUME,
                        style = MaterialTheme.typography.body2,
                        modifier = Modifier.padding(0.dp, 8.dp)
                    )
                }

                ColumnRoundedContainer {
                    Text("Introduction", style = MaterialTheme.typography.h6)
                    Text(
                        text = LifeMarkIntroduction.INTRODUCTION,
                        style = MaterialTheme.typography.body2,
                        modifier = Modifier.padding(0.dp, 8.dp)
                    )
                }

                ColumnRoundedContainer {
                    Text("Key Features", style = MaterialTheme.typography.h6)
                    LifeMarkIntroduction.MAINS.forEach { line ->
                        Spacer(Modifier.height(10.dp))
                        Text(line, style = MaterialTheme.typography.body2)
                    }
                }

                Text(
                    LifeMarkIntroduction.COPYRIGHT,
                    style = MaterialTheme.typography.caption,
                    color = Color.LightGray,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth().padding(bottom = 60.dp)
                )
            }
        }
    }
}