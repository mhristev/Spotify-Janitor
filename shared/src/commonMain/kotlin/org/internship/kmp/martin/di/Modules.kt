package org.internship.kmp.martin.di

expect val platformModule: Module
//
//val sharedModule = module {
//    single { HttpClientFactory.create(get()) }
//    singleOf(::KtorRemoteBookDataSource).bind<RemoteBookDataSource>()
//    singleOf(::DefaultBookRepository).bind<BookRepository>()
//
//    single {
//        get<DatabaseFactory>().create()
//            .setDriver(BundledSQLiteDriver())
//            .build()
//    }
//    single { get<FavoriteBookDatabase>().favoriteBookDao }
//
//    viewModelOf(::BookListViewModel)
//    viewModelOf(::BookDetailViewModel)
//    viewModelOf(::SelectedBookViewModel)
//}