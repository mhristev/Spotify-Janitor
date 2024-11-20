package org.internship.kmp.martin.di

fun initKoin(config: KoinAppDeclaration? = null) {
    startKoin {
        config?.invoke(this)
        modules(sharedModule, platformModule)
    }
}


//override fun onCreate() {
//    super.onCreate()
//    initKoin {
//        androidContext(this@BookApplication)
//    }
//}

// Manifest
// android:name=".BookApplication"


// ios injection of viewmodel
// viewModel = koinViewModel<BookListViewModel>()