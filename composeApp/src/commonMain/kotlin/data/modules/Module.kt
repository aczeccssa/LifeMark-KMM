package data.modules

import cache.DatabaseDriverFactory
import data.network.SpaceXApi
import data.sdk.SpaceXSDK
import org.koin.core.context.startKoin
import org.koin.core.module.Module
import org.koin.dsl.module
import screens.experimental.SpaceXLauncherHistoryViewModel
import viewmodel.ProfileScreenViewModel

private val viewModelModule: Module = module {
    // MARK: ProfileScreenViewModel
    single<ProfileScreenViewModel> {
        ProfileScreenViewModel()
    }

    // MARK: SpaceXLauncherHistoryViewModel
    single<SpaceXLauncherHistoryViewModel> {
        SpaceXLauncherHistoryViewModel(
            SpaceXSDK(databaseDriverFactory = get(), SpaceXApi())
        )
    }
}

private val databaseModule: Module = module {
    // MARK: DatabaseDriverFactory
    single<DatabaseDriverFactory> {
        DatabaseDriverFactory()
    }
}

// Koin init handle
fun initKoin() {
    // Koin
    startKoin {
        modules(viewModelModule, databaseModule)
    }
}