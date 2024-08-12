package screens.register

import lifemark_kmm.composeapp.generated.resources.Res
import lifemark_kmm.composeapp.generated.resources.emoji_camera_animated
import lifemark_kmm.composeapp.generated.resources.emoji_party_animated
import lifemark_kmm.composeapp.generated.resources.emoji_partying_animated
import org.jetbrains.compose.resources.DrawableResource

enum class RegisterPhase {
    PROCESS_JOIN {
        override val drawableResource get() = Res.drawable.emoji_camera_animated

        override val titlePair: Pair<String, String> = Pair("Join community", "Now your finances are in one place and always under control")
    },
    REGISTER {
        override val drawableResource get() = Res.drawable.emoji_partying_animated

        override val titlePair: Pair<String, String?> = Pair("Create account", null)
    },
    SIGN_IN {
        override val drawableResource get() = Res.drawable.emoji_party_animated

        override val titlePair: Pair<String, String?> = Pair("Welcome back", null)
    };

    abstract val drawableResource: DrawableResource

    abstract val titlePair: Pair<String, String?>
}
