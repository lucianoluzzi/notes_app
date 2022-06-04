package com.task.noteapp.dependencyInjection

import androidx.room.Room
import nl.com.lucianoluzzi.data.NoteDatabase
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val databaseModule = module {
    single {
        Room.databaseBuilder(
            androidContext(),
            NoteDatabase::class.java, "note-db"
        ).build()
    }

    single {
        get<NoteDatabase>().getNoteDao()
    }
}
