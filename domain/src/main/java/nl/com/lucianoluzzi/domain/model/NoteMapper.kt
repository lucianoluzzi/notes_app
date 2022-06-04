package nl.com.lucianoluzzi.domain.model

import nl.com.lucianoluzzi.data.entity.NoteEntity

class NoteMapper {
    fun mapNoteEntityListToNoteList(noteEntities: List<NoteEntity>): List<Note> {
        return noteEntities.map {
            mapNoteEntityToNote(it)
        }
    }

    fun mapNoteEntityToNote(noteEntity: NoteEntity) = Note(
        id = noteEntity.id,
        title = noteEntity.title,
        description = noteEntity.description,
        createdAtDate = noteEntity.createdAt,
        editedAtDate = noteEntity.editedAt,
        imageUrl = noteEntity.imageUrl
    )

    fun mapNoteToNoteEntity(note: Note) = NoteEntity(
        id = note.id ?: 0,
        title = note.title,
        description = note.description,
        imageUrl = note.imageUrl,
        createdAt = note.createdAtDate,
        editedAt = note.editedAtDate,
    )
}
