package com.task.noteapp

import android.app.Application
import com.task.noteapp.dependencyInjection.*
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.fragment.koin.fragmentFactory
import org.koin.core.context.startKoin

class NotesApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        initDependencyInjection()
    }

    private fun initDependencyInjection() {
        startKoin {
            fragmentFactory()
            androidContext(this@NotesApplication)
            val modules = listOf(
                viewModelModule,
                fragmentModule,
                useCaseModule,
                mapperModule,
                databaseModule,
                repositoryModule,
                navigationModule,
                serviceModule,
            )
            modules(modules)
        }
    }
}
