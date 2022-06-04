package nl.com.lucianoluzzi.domain.useCase

import nl.com.lucianoluzzi.domain.NoteRepository
import nl.com.lucianoluzzi.domain.service.ErrorTracker

class DeleteNoteUseCase(
    private val errorTracker: ErrorTracker,
    private val noteRepository: NoteRepository,
) {
    suspend operator fun invoke(noteId: Long) {
        try {
            noteRepository.deleteNote(noteId)
        } catch (exception: Exception) {
            errorTracker.trackError(exception)
        }
    }
}
