package data.modules

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.lifecycle.ViewModel
import com.benasher44.uuid.Uuid
import com.benasher44.uuid.uuidFrom
import org.koin.core.parameter.ParametersDefinition
import org.koin.core.qualifier.Qualifier
import org.koin.mp.KoinPlatform.getKoin


@Composable
inline fun <reified T : ViewModel> getViewModel(
    qualifier: Qualifier? = null,
    noinline parameters: ParametersDefinition? = null
): T {
    val koin = getKoin()
    return remember { koin.get(qualifier, parameters) }
}

val DEFAULT_AVATAR_RESOURCES_IDENTIFIER: Uuid = uuidFrom("aabbccdd-1111-eeff-2222-afbecddcebda")