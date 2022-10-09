package nl.com.lucianoluzzi.notes.ui

import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import nl.com.lucianoluzzi.domain.model.Note
import nl.com.lucianoluzzi.domain.model.Response
import nl.com.lucianoluzzi.domain.useCase.GetNotesUseCase
import nl.com.lucianoluzzi.notes.domain.NoteUiMapper
import nl.com.lucianoluzzi.notes.domain.model.NoteUiModel
import nl.com.lucianoluzzi.notes.domain.model.NotesState
import nl.com.lucianoluzzi.testresources.extensions.IMAGE_URL
import nl.com.lucianoluzzi.testresources.extensions.InstantExecutorExtension
import nl.com.lucianoluzzi.testresources.extensions.TestDispatcherExtension
import org.junit.jupiter.api.*
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.kotlin.any
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import java.util.*

@ExperimentalCoroutinesApi
@ExtendWith(InstantExecutorExtension::class, TestDispatcherExtension::class)
internal class NotesViewModelTest {
    private val noteUiMapper = mock<NoteUiMapper>()
    private val getNotesUseCase = mock<GetNotesUseCase>()

    private val initialNotesState = NotesState.NotesList(
        notes = emptyList(),
        showNoteDetail = null,
        createNewNote = false
    )

    @Nested
    @DisplayName("Given ViewModel created")
    inner class Init {
        private val viewModel = NotesViewModel(
            noteUiMapper = noteUiMapper,
            getNotesUseCase = getNotesUseCase,
        )

        @Test
        fun `then notes live data emit NotesState`() {
            val notesState = viewModel.notesState
            assertThat(notesState.value).isNotNull()
            assertThat(notesState.value is NotesState).isTrue()
        }

        @Test
        fun `then notes live data emit the initial state`() {
            if (viewModel.notesState.value is NotesState) {
                val notesState = viewModel.notesState.value as NotesState
                assertThat(notesState).isEqualTo(initialNotesState)
            } else {
                fail("Expected viewModel.notes.value to be NotesState but it was ${viewModel.notesState.value?.javaClass} instead")
            }
        }

        @Nested
        @DisplayName("When get notes use case returns an empty flow")
        inner class EmptyList {

            @BeforeEach
            private fun setUpTests() {
                val emptyListFlow = flowOf(emptyList<Note>())
                whenever(getNotesUseCase("")).thenReturn(
                    Response.Success(emptyListFlow)
                )
            }

            @Test
            fun `then an empty list is emitted`() {
                val viewModel = NotesViewModel(
                    noteUiMapper = noteUiMapper,
                    getNotesUseCase = getNotesUseCase,
                )

                val notesState = viewModel.notesState.value

                assertThat(notesState).isNotNull()
                assertThat(notesState is NotesState.NotesList).isTrue()
                val notesList = notesState as NotesState.NotesList
                assertThat(notesList.notes).isEmpty()
            }
        }

        @Nested
        @DisplayName("When get notes use case returns a list of notes")
        inner class NotesList {
            private val notes = listOf(
                Note(
                    title = "First note",
                    description = "I'm the first note",
                    createdAtDate = Date(),
                    imageUrl = IMAGE_URL
                ),
                Note(
                    title = "#2",
                    description = "Dois",
                    createdAtDate = Date(),
                    editedAtDate = Date(),
                )
            )
            private val noteUiList = listOf(
                NoteUiModel(
                    title = "",
                    description = "",
                    createdAtDate = "18/04/1989",
                    isEdited = true,
                    imageUrl = IMAGE_URL,
                ),
                NoteUiModel(
                    title = "2",
                    description = "2",
                    createdAtDate = "18/04/1989",
                    isEdited = false,
                ),
            )

            @BeforeEach
            private fun setUpTests() {
                val notesFlow = flowOf(notes)
                whenever(getNotesUseCase("")).thenReturn(
                    Response.Success(notesFlow)
                )
                whenever(noteUiMapper.mapNotesToNoteUiModelList(any())).thenReturn(noteUiList)
            }

            @Test
            fun `then the noteUiMapper is called with the returned note list`() {
                NotesViewModel(
                    noteUiMapper = noteUiMapper,
                    getNotesUseCase = getNotesUseCase,
                )
                verify(noteUiMapper).mapNotesToNoteUiModelList(notes)
            }

            @Test
            fun `then an note ui list is emitted`() {
                val viewModel = NotesViewModel(
                    noteUiMapper = noteUiMapper,
                    getNotesUseCase = getNotesUseCase,
                )
                val notesState = viewModel.notesState.value

                assertThat(notesState).isNotNull()
                assertThat(notesState is NotesState.NotesList).isTrue()
                val notesList = notesState as NotesState.NotesList
                assertThat(notesList.notes).isEqualTo(noteUiList)
            }
        }

        @Nested
        @DisplayName("When get notes use case returns an error")
        inner class Error {
            @Test
            fun `then emits NotesState Error`() {
                whenever(getNotesUseCase("")).thenReturn(Response.Error())

                val viewModel = NotesViewModel(
                    noteUiMapper = noteUiMapper,
                    getNotesUseCase = getNotesUseCase,
                )
                val notesState = viewModel.notesState.value

                assertThat(notesState is NotesState.Error).isTrue()
            }
        }
    }

    @Nested
    @DisplayName("Given onCreateClick")
    inner class CreateClick {
        private val viewModel = NotesViewModel(
            noteUiMapper = noteUiMapper,
            getNotesUseCase = getNotesUseCase,
        )

        @Test
        fun `then NotesState is emitted with createNewNote as true`() {
            viewModel.onCreateClick()

            val notesState = viewModel.notesState.value

            assertThat(notesState is NotesState.NotesList).isTrue()
            val notesList = notesState as NotesState.NotesList
            assertThat(notesList.createNewNote).isTrue()
        }
    }

    @Nested
    @DisplayName("Given onNoteClick")
    inner class NoteClick {
        @Test
        fun `then NotesListState is emitted with ShowNoteDetail and noteId`() = runTest {
            val viewModel = NotesViewModel(
                noteUiMapper = noteUiMapper,
                getNotesUseCase = getNotesUseCase,
            )
            val noteId = 0L

            viewModel.onNoteClick(noteId)
            val notesState = viewModel.notesState.value
            assertThat(notesState is NotesState.NotesList).isTrue()
            val notesList = notesState as NotesState.NotesList
            assertThat(notesList.showNoteDetail?.noteId).isEqualTo(noteId)
        }
    }

    @Nested
    @DisplayName("Given hasNavigatedToNoteDetails")
    inner class NavigatedToNoteDetails {
        @Test
        fun `then emits NotesState with createNewNote and showNoteDetails equals to the initial state`() {
            val viewModel = NotesViewModel(
                noteUiMapper = noteUiMapper,
                getNotesUseCase = getNotesUseCase,
            )

            viewModel.hasNavigatedToNoteDetail()
            val notesState = viewModel.notesState.value

            assertThat(notesState is NotesState.NotesList).isTrue()
            val notesList = notesState as NotesState.NotesList
            assertThat(notesList.createNewNote).isEqualTo(initialNotesState.createNewNote)
            assertThat(notesList.showNoteDetail).isEqualTo(initialNotesState.showNoteDetail)
        }
    }
}
