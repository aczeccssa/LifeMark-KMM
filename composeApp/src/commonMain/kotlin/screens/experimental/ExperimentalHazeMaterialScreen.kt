package screens.experimental

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import components.NavigationHeader
import components.Rectangle
import data.NavigationHeaderConfiguration
import dev.chrisbanes.haze.HazeState
import dev.chrisbanes.haze.haze
import dev.chrisbanes.haze.hazeChild
import dev.chrisbanes.haze.materials.ExperimentalHazeMaterialsApi
import dev.chrisbanes.haze.materials.HazeMaterials
import lifemark_kmm.composeapp.generated.resources.Res
import lifemark_kmm.composeapp.generated.resources.haze_material_banner
import org.jetbrains.compose.resources.painterResource

object ExperimentalHazeMaterialScreen : Screen {
    @OptIn(ExperimentalHazeMaterialsApi::class)
    @Composable
    override fun Content() {
        val hazeState = remember { HazeState() }

        Surface {
            NavigationHeader(
                title = "Global Sheep",
                configuration = NavigationHeaderConfiguration.transparentConfiguration,
                modifier = Modifier.hazeChild(hazeState, style = HazeMaterials.ultraThin())
            )

            MaterialsSample(hazeState)
        }
    }

    @Composable
    private fun MaterialsSample(hazeState: HazeState) {
        Box {
            Image(
                painter = painterResource(Res.drawable.haze_material_banner),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier.haze(hazeState).fillMaxSize()
            )

            Column(Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(24.dp)) {
                Spacer(Modifier.height(400.dp))

                MaterialsRow(hazeState, Modifier.fillMaxWidth(),)
            }
        }
    }

    @OptIn(ExperimentalLayoutApi::class)
    @Composable
    private fun MaterialsRow(hazeState: HazeState, modifier: Modifier = Modifier) {
        FlowRow(modifier, Arrangement.spacedBy(16.dp),) {
            MaterialsCard("Ultra Thin", hazeState)

            MaterialsCard("Thin", hazeState)

            MaterialsCard("Regular", hazeState)

            MaterialsCard("Thick", hazeState)

            MaterialsCard("Ultra Thick", hazeState)
        }
    }

    @OptIn(ExperimentalHazeMaterialsApi::class)
    @Composable
    private fun MaterialsCard(name: String, hazeState: HazeState) {
        Rectangle(
            size = DpSize(160.dp, 160.dp),
            modifier = Modifier
                .clip(RoundedCornerShape(12.dp))
                .hazeChild(hazeState, style = HazeMaterials.thin())
        ) {
            Text(name, modifier = Modifier.padding(16.dp))
        }
    }
}