package com.task.noteapp.dependencyInjection

import nl.com.lucianoluzzi.notedetails.ui.NoteDetailViewModel
import nl.com.lucianoluzzi.notes.ui.NotesViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    viewModel {
        NotesViewModel(
            noteUiMapper = get(),
            getNotesUseCase = get(),
        )
    }

    viewModel { params ->
        NoteDetailViewModel(
            noteId = params.getOrNull(),
            getNoteUseCase = get(),
            saveNoteUseCase = get(),
            deleteNoteUseCase = get(),
            formDataMapper = get(),
        )
    }
}
