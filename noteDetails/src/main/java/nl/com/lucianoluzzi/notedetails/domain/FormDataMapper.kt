package nl.com.lucianoluzzi.notedetails.domain

import nl.com.lucianoluzzi.domain.model.Note
import nl.com.lucianoluzzi.notedetails.domain.model.FormData

class FormDataMapper {
    fun mapNoteToFormData(note: Note) = FormData(
        id = note.id,
        title = note.title,
        description = note.description,
        imageUrl = note.imageUrl ?: ""
    )
}
