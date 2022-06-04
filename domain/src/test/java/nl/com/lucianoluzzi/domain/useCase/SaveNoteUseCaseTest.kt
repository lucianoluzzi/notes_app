package nl.com.lucianoluzzi.domain.useCase

import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import nl.com.lucianoluzzi.domain.NoteRepository
import nl.com.lucianoluzzi.domain.model.Note
import nl.com.lucianoluzzi.domain.service.ErrorTracker
import nl.com.lucianoluzzi.testresources.extensions.IMAGE_URL
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.mockito.kotlin.argumentCaptor
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import java.util.*

@ExperimentalCoroutinesApi
internal class SaveNoteUseCaseTest {
    private val noteRepository = mock<NoteRepository>()
    private val errorTracker = mock<ErrorTracker>()
    private val saveNoteUseCase = SaveNoteUseCase(
        noteRepository = noteRepository,
        errorTracker = errorTracker,
    )

    @Nested
    @DisplayName("When it's called with note id")
    inner class WithNodeId {
        private val noteId = 1L

        @Test
        fun `then repository called with update and expected Note`() = runTest {
            val title = "title"
            val description = "test"
            val imageUrl = IMAGE_URL
            val noteCreationDate = Calendar.getInstance().apply {
                set(1999, 2, 10)
            }.time
            val expectedNote = Note(
                id = noteId,
                title = title,
                description = description,
                createdAtDate = noteCreationDate,
                imageUrl = imageUrl
            )

            whenever(noteRepository.getCreationDate(noteId)).thenReturn(noteCreationDate)
            saveNoteUseCase(
                noteId = noteId,
                title = title,
                description = description,
                imageUrl = imageUrl,
            )
            val argumentCaptor = argumentCaptor<Note>()
            verify(noteRepository).updateNote(argumentCaptor.capture())
            val calledParameter = argumentCaptor.firstValue

            assertThat(calledParameter.id).isEqualTo(expectedNote.id)
            assertThat(calledParameter.title).isEqualTo(expectedNote.title)
            assertThat(calledParameter.description).isEqualTo(expectedNote.description)
            assertThat(calledParameter.imageUrl).isEqualTo(expectedNote.imageUrl)
            assertThat(calledParameter.createdAtDate).isEqualTo(expectedNote.createdAtDate)
        }
    }

    @Nested
    @DisplayName("When note id is null")
    inner class NoteIdNull {
        @Test
        fun `then repository called with save and expected Note`() = runTest {
            val title = "another title"
            val description = "test again"
            val imageUrl = IMAGE_URL
            val noteCreationDate = Date()
            val expectedNote = Note(
                title = title,
                description = description,
                createdAtDate = noteCreationDate,
                editedAtDate = null,
                imageUrl = imageUrl,
            )

            saveNoteUseCase(
                noteId = null,
                title = title,
                description = description,
                imageUrl = imageUrl,
            )

            verify(noteRepository).saveNote(expectedNote)
        }
    }
}
