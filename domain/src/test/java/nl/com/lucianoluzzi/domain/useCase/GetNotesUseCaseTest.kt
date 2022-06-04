package nl.com.lucianoluzzi.domain.useCase

import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import nl.com.lucianoluzzi.data.entity.NoteEntity
import nl.com.lucianoluzzi.domain.NoteRepository
import nl.com.lucianoluzzi.domain.model.Note
import nl.com.lucianoluzzi.domain.model.NoteMapper
import nl.com.lucianoluzzi.domain.model.Response
import nl.com.lucianoluzzi.domain.service.ErrorTracker
import nl.com.lucianoluzzi.testresources.extensions.IMAGE_URL
import nl.com.lucianoluzzi.testresources.extensions.InstantExecutorExtension
import org.junit.jupiter.api.*
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.kotlin.doThrow
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import java.io.IOException
import java.util.*

@ExperimentalCoroutinesApi
@ExtendWith(InstantExecutorExtension::class)
internal class GetNotesUseCaseTest {
    private val noteRepository = mock<NoteRepository>()
    private val noteMapper = mock<NoteMapper>()
    private val errorTracker = mock<ErrorTracker>()
    private val getNotesUseCase = GetNotesUseCase(
        noteRepository = noteRepository,
        noteMapper = noteMapper,
        errorTracker = errorTracker,
    )

    @Nested
    @DisplayName("Given exception is thrown")
    inner class ExceptionIsThrown {
        private val exception = IOException("Database error")

        @BeforeEach
        private fun setUp() {
            whenever(noteRepository.getNotes()).doThrow(exception)
        }

        @Test
        fun `then returns response Error`() = runTest {
            val notesResponse = getNotesUseCase.invoke()
            assertThat(notesResponse is Response.Error).isTrue()
        }

        @Test
        fun `then error tracker track error`() {
            getNotesUseCase.invoke()
            verify(errorTracker).trackError(exception)
        }
    }

    @Nested
    @DisplayName("Given repository returns a list of note entities")
    inner class ListOfNotes {
        private val noteEntities = listOf(
            NoteEntity(
                id = 1,
                title = "title",
                description = "I'm one note",
                createdAt = Date(),
                editedAt = Date(),
            ),
            NoteEntity(
                id = 2,
                title = "",
                description = "I'm another",
                createdAt = Date(),
                imageUrl = IMAGE_URL,
            ),
        )
        private val noteList = listOf(
            Note(
                id = 1L,
                title = "first note",
                description = "#1",
                createdAtDate = Calendar.getInstance().apply {
                    set(2020, 1, 15)
                }.time,
                imageUrl = IMAGE_URL,
            ),
            Note(
                id = 3L,
                title = "note",
                description = "três",
                createdAtDate = Calendar.getInstance().apply {
                    set(2022, 3, 20)
                }.time,
                imageUrl = IMAGE_URL,
            ),
            Note(
                id = 5L,
                title = "",
                description = "",
                createdAtDate = Calendar.getInstance().apply {
                    set(2049, 0, 0)
                }.time,
                imageUrl = IMAGE_URL,
            )
        )
        private lateinit var noteUseCase: GetNotesUseCase

        @BeforeEach
        private fun setUp() {
            val noteEntitiesFlow = flowOf(noteEntities)
            whenever(noteRepository.getNotes()).thenReturn(noteEntitiesFlow)
            whenever(noteMapper.mapNoteEntityListToNoteList(noteEntities)).thenReturn(noteList)
            noteUseCase = GetNotesUseCase(
                errorTracker = errorTracker,
                noteRepository = noteRepository,
                noteMapper = noteMapper,
            )
        }

        @Test
        fun `then noteMapper is called with the repository result`() = runTest {
            val response = noteUseCase.invoke()

            if (response is Response.Success) {
                response.data?.collect()
                verify(noteMapper).mapNoteEntityListToNoteList(noteEntities)
            } else {
                fail("Expected response to be Response.Success but it was ${response.javaClass} instead")
            }
        }

        @Test
        fun `then returned flow is the same as noteMapper result ordered by date descending`() =
            runTest {
                val expectedResult = noteList.sortedByDescending {
                    it.createdAtDate
                }
                val notesResult = mutableListOf<Note>()
                val response = noteUseCase()
                if (response is Response.Success) {
                    response.data?.collect {
                        notesResult.addAll(it)
                    }
                    assertThat(notesResult).isEqualTo(expectedResult)
                } else {
                    fail("Expected respose to be Response.Success but it was ${response.javaClass} instead")
                }
            }
    }
}
