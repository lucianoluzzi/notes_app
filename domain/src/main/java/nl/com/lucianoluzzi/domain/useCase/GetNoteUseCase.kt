package nl.com.lucianoluzzi.domain.useCase

import nl.com.lucianoluzzi.domain.NoteRepository
import nl.com.lucianoluzzi.domain.model.Note
import nl.com.lucianoluzzi.domain.model.NoteMapper
import nl.com.lucianoluzzi.domain.model.Response
import nl.com.lucianoluzzi.domain.service.ErrorTracker

class GetNoteUseCase(
    private val errorTracker: ErrorTracker,
    private val noteRepository: NoteRepository,
    private val noteMapper: NoteMapper,
) {
    suspend operator fun invoke(noteId: Long?): Response<Note?> {
        return noteId?.let {
            getNoteOrCatch(it)
        } ?: Response.Success()
    }

    private suspend fun getNoteOrCatch(noteId: Long): Response<Note?> {
        return try {
            val note = noteRepository.getNote(noteId)?.let {
                noteMapper.mapNoteEntityToNote(it)
            }
            Response.Success(note)
        } catch (exception: Exception) {
            errorTracker.trackError(exception)
            Response.Error()
        }
    }
}
