package com.task.noteapp.dependencyInjection

import nl.com.lucianoluzzi.notes.ui.NotesFragment
import org.koin.androidx.fragment.dsl.fragment
import org.koin.dsl.module

val fragmentModule = module {
    fragment {
        NotesFragment(
            viewModel = get(),
            navigator = get()
        )
    }
}
