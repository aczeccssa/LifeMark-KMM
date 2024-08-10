package data.interfaces

import org.jetbrains.compose.resources.DrawableResource

interface Platform {
    val name: String
    val version: String

    val logo: DrawableResource
}