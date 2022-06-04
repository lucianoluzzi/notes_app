package nl.com.lucianoluzzi.domain.useCase

import nl.com.lucianoluzzi.domain.NoteRepository
import nl.com.lucianoluzzi.domain.model.Note
import nl.com.lucianoluzzi.domain.service.ErrorTracker
import java.util.*

class SaveNoteUseCase(
    private val errorTracker: ErrorTracker,
    private val noteRepository: NoteRepository,
) {
    suspend operator fun invoke(
        noteId: Long?,
        title: String,
        description: String,
        imageUrl: String?
    ) {
        try {
            if (noteId != null) {
                val note = Note(
                    id = noteId,
                    title = title,
                    description = description,
                    imageUrl = imageUrl,
                    createdAtDate = noteRepository.getCreationDate(noteId) ?: Date(),
                    editedAtDate = Date(),
                )
                noteRepository.updateNote(note)
            } else {
                val note = Note(
                    id = noteId,
                    title = title,
                    description = description,
                    imageUrl = imageUrl,
                    createdAtDate = Date(),
                )
                noteRepository.saveNote(note)
            }
        } catch (exception: Exception) {
            errorTracker.trackError(exception)
        }
    }
}
