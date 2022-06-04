package com.task.noteapp.dependencyInjection

import nl.com.lucianoluzzi.domain.service.ErrorTracker
import nl.com.lucianoluzzi.domain.service.ErrorTrackerImpl
import org.koin.dsl.module

val serviceModule = module {
    single<ErrorTracker> {
        ErrorTrackerImpl()
    }
}
