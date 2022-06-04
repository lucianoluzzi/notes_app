package nl.com.lucianoluzzi.notes.domain

import nl.com.lucianoluzzi.domain.model.Note
import nl.com.lucianoluzzi.domain.useCase.FormatDateUseCase
import nl.com.lucianoluzzi.notes.domain.model.NoteUiModel

class NoteUiMapper(
    private val formatDateUseCase: FormatDateUseCase
) {
    fun mapNotesToNoteUiModelList(notes: List<Note>): List<NoteUiModel> {
        return notes.map {
            mapNoteToNoteUiModel(it)
        }
    }

    private fun mapNoteToNoteUiModel(note: Note) = NoteUiModel(
        id = note.id,
        title = note.title,
        description = note.description,
        imageUrl = note.imageUrl,
        createdAtDate = formatDateUseCase(note.createdAtDate),
        isEdited = note.editedAtDate != null
    )
}
