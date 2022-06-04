package com.task.noteapp.dependencyInjection

import nl.com.lucianoluzzi.domain.useCase.*
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val useCaseModule = module {
    single {
        val locale = androidContext().resources.configuration.locales[0]
        FormatDateUseCase(locale = locale)
    }

    single {
        GetNotesUseCase(
            errorTracker = get(),
            noteRepository = get(),
            noteMapper = get(),
        )
    }

    single {
        GetNoteUseCase(
            errorTracker = get(),
            noteRepository = get(),
            noteMapper = get(),
        )
    }

    single {
        SaveNoteUseCase(
            errorTracker = get(),
            noteRepository = get(),
        )
    }

    single {
        DeleteNoteUseCase(
            errorTracker = get(),
            noteRepository = get(),
        )
    }
}
