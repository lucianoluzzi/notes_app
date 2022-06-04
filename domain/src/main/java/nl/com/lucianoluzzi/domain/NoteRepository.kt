package nl.com.lucianoluzzi.domain

import nl.com.lucianoluzzi.data.dao.NoteDao
import nl.com.lucianoluzzi.data.entity.NoteEntity
import nl.com.lucianoluzzi.domain.model.Note
import nl.com.lucianoluzzi.domain.model.NoteMapper
import java.util.*

class NoteRepository(
    private val noteDao: NoteDao,
    private val noteMapper: NoteMapper,
) {
    @Throws(Exception::class)
    fun getNotes() = noteDao.getNotes()

    @Throws(Exception::class)
    suspend fun getNote(id: Long): NoteEntity? = noteDao.getNote(id)

    @Throws(Exception::class)
    suspend fun getCreationDate(id: Long): Date? = noteDao.getCreationDate(id)

    @Throws(Exception::class)
    suspend fun saveNote(note: Note) {
        val noteEntity = noteMapper.mapNoteToNoteEntity(note)
        noteDao.insert(noteEntity)
    }

    @Throws(Exception::class)
    suspend fun updateNote(note: Note) {
        val noteEntity = noteMapper.mapNoteToNoteEntity(note)
        noteDao.update(noteEntity)
    }

    @Throws(Exception::class)
    suspend fun deleteNote(noteId: Long) {
        noteDao.delete(noteId)
    }
}
