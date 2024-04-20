package data.interfaces

import lifemark_kmm.composeapp.generated.resources.Res
import lifemark_kmm.composeapp.generated.resources.swift_logo_white
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.ExperimentalResourceApi
import platform.UIKit.UIDevice

class IOSPlatform : Platform {
    override val name: String = UIDevice.currentDevice.systemName()
    
    override val version: String = UIDevice.currentDevice.systemVersion
    
    @OptIn(ExperimentalResourceApi::class)
    override val logo: DrawableResource = Res.drawable.swift_logo_white
}
