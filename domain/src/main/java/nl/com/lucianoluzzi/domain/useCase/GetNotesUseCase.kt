package nl.com.lucianoluzzi.domain.useCase

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import nl.com.lucianoluzzi.domain.NoteRepository
import nl.com.lucianoluzzi.domain.model.Note
import nl.com.lucianoluzzi.domain.model.NoteMapper
import nl.com.lucianoluzzi.domain.model.Response
import nl.com.lucianoluzzi.domain.service.ErrorTracker

class GetNotesUseCase(
    private val errorTracker: ErrorTracker,
    private val noteRepository: NoteRepository,
    private val noteMapper: NoteMapper,
) {
    operator fun invoke(): Response<Flow<List<Note>>> {
        return try {
            val flowOfNotes = noteRepository.getNotes().map { noteEntityList ->
                noteMapper.mapNoteEntityListToNoteList(noteEntityList).sortedByDescending {
                    it.createdAtDate.time
                }
            }
            Response.Success(flowOfNotes)
        } catch (exception: Exception) {
            errorTracker.trackError(exception)
            Response.Error()
        }
    }
}
