package com.task.noteapp.dependencyInjection

import nl.com.lucianoluzzi.navigation.Navigator
import nl.com.lucianoluzzi.navigationimplementation.NavigatorImpl
import org.koin.dsl.module

val navigationModule = module {
    single<Navigator> {
        NavigatorImpl()
    }
}
