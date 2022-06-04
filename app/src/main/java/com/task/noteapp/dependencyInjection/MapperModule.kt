package com.task.noteapp.dependencyInjection

import nl.com.lucianoluzzi.domain.model.NoteMapper
import nl.com.lucianoluzzi.notedetails.domain.FormDataMapper
import nl.com.lucianoluzzi.notes.domain.NoteUiMapper
import org.koin.dsl.module

val mapperModule = module {
    single {
        NoteUiMapper(formatDateUseCase = get())
    }

    single {
        NoteMapper()
    }

    single {
        FormDataMapper()
    }
}
