package com.task.noteapp.dependencyInjection

import nl.com.lucianoluzzi.domain.NoteRepository
import org.koin.dsl.module

val repositoryModule = module {
    single {
        NoteRepository(
            noteMapper = get(),
            noteDao = get(),
        )
    }
}
