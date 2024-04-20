package data.interfaces

import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.ExperimentalResourceApi

interface Platform {
    val name: String
    val version: String
    
    @OptIn(ExperimentalResourceApi::class)
    val logo: DrawableResource
}