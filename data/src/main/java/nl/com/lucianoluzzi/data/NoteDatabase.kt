package nl.com.lucianoluzzi.data

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import nl.com.lucianoluzzi.data.converter.DateConverter
import nl.com.lucianoluzzi.data.dao.NoteDao
import nl.com.lucianoluzzi.data.entity.NoteEntity

@Database(
    version = 1,
    entities = [
        NoteEntity::class
    ],
    exportSchema = true,
)
@TypeConverters(DateConverter::class)
abstract class NoteDatabase : RoomDatabase() {
    abstract fun getNoteDao(): NoteDao
}
