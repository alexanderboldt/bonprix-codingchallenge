package org.challenge.configuration

import org.challenge.api.ApiClient
import org.koin.core.context.startKoin
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

internal val apiClientModule = module {
    singleOf(::ApiClient)
}

internal fun initializeKoin() {
    startKoin {
        modules(apiClientModule)
    }
}

