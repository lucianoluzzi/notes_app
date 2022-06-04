package nl.com.lucianoluzzi.notedetails.ui

import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import nl.com.lucianoluzzi.domain.model.Note
import nl.com.lucianoluzzi.domain.model.Response
import nl.com.lucianoluzzi.domain.useCase.DeleteNoteUseCase
import nl.com.lucianoluzzi.domain.useCase.GetNoteUseCase
import nl.com.lucianoluzzi.domain.useCase.SaveNoteUseCase
import nl.com.lucianoluzzi.notedetails.domain.FormDataMapper
import nl.com.lucianoluzzi.notedetails.domain.model.FormData
import nl.com.lucianoluzzi.notedetails.domain.model.NoteDetailIntent
import nl.com.lucianoluzzi.notedetails.domain.model.NoteDetailState
import nl.com.lucianoluzzi.testresources.extensions.IMAGE_URL
import nl.com.lucianoluzzi.testresources.extensions.InstantExecutorExtension
import nl.com.lucianoluzzi.testresources.extensions.TestDispatcherExtension
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.verifyNoInteractions
import org.mockito.kotlin.whenever
import java.util.*

@ExperimentalCoroutinesApi
@ExtendWith(InstantExecutorExtension::class, TestDispatcherExtension::class)
internal class NoteDetailViewModelTest {
    private val getNoteUseCase = mock<GetNoteUseCase>()
    private val saveNoteUseCase = mock<SaveNoteUseCase>()
    private val deleteNoteUseCase = mock<DeleteNoteUseCase>()
    private val formDataMapper = mock<FormDataMapper>()

    @Nested
    @DisplayName("Given noteId is null")
    inner class NoteIdNull {

        @Nested
        @DisplayName("When ViewModel is created")
        inner class Init {
            private lateinit var viewModel: NoteDetailViewModel

            @BeforeEach
            private fun setUp() {
                viewModel = getViewModel()
            }

            @Test
            fun `then form state emits FormDetails with null initial form data`() {
                val formState = viewModel.formState.value

                assertThat(formState).isNotNull()
                assertThat(formState is NoteDetailState.FormDetails).isTrue()
                val formDetailsState = formState as NoteDetailState.FormDetails
                assertThat(formDetailsState.formData).isNull()
            }

            @Test
            fun `then form state emits with delete button disabled`() {
                val formState = viewModel.formState.value

                assertThat(formState is NoteDetailState.FormDetails).isTrue()
                val formDetailsState = formState as NoteDetailState.FormDetails
                assertThat(formDetailsState.isDeleteButtonEnabled).isFalse()
            }
        }

        @Nested
        @DisplayName("When delete intent is received")
        inner class OnDeleteIntent {
            @Test
            fun `then delete use case is not called`() {
                val viewModel = getViewModel()
                viewModel.onIntent(NoteDetailIntent.Delete)

                verifyNoInteractions(deleteNoteUseCase)
            }
        }

        @Nested
        @DisplayName("When save intent is received")
        inner class OnSaveIntent {
            @Test
            fun `then save note use case is called with intent parameters and note id`() = runTest {
                val viewModel = getViewModel()
                val saveIntent = NoteDetailIntent.Save(
                    title = "title",
                    description = "description",
                    imageUrl = IMAGE_URL,
                )

                viewModel.onIntent(saveIntent)

                verify(saveNoteUseCase).invoke(
                    noteId = null,
                    title = saveIntent.title,
                    description = saveIntent.description,
                    imageUrl = saveIntent.imageUrl,
                )
            }

            @Test
            fun `then NoteSaved intent is emitted`() {
                val viewModel = getViewModel()
                val saveIntent = NoteDetailIntent.Save(
                    title = "title",
                    description = "description",
                    imageUrl = IMAGE_URL,
                )

                viewModel.onIntent(saveIntent)
                val formState = viewModel.formState.value

                assertThat(formState is NoteDetailState.NoteSaved).isTrue()
            }
        }

        @Nested
        @DisplayName("When text input intent is received")
        inner class OnTextInputIntent {
            private val initialFormData: FormData? = null
            private val viewModel = getViewModel()

            @Test
            fun `if form changed and mandatory fields are not empty, then emits new form state with save button enabled`() {
                val formChangedIntent = NoteDetailIntent.OnTextInput(
                    title = "title",
                    description = "description",
                    imageUrl = IMAGE_URL,
                )

                viewModel.onIntent(formChangedIntent)
                val formState = viewModel.formState.value

                assertThat(formState).isNotNull()
                assertThat(formState is NoteDetailState.FormDetails).isTrue()
                val formDetailsState = formState as NoteDetailState.FormDetails
                assertThat(formDetailsState.isSaveButtonEnabled).isTrue()
            }

            @Test
            fun `if form changed but mandatory fields are empty, then emits new form state with save button enabled`() {
                val formChangedIntent = NoteDetailIntent.OnTextInput(
                    title = "title",
                    description = "",
                    imageUrl = IMAGE_URL,
                )

                viewModel.onIntent(formChangedIntent)
                val formState = viewModel.formState.value

                assertThat(formState).isNotNull()
                assertThat(formState is NoteDetailState.FormDetails).isTrue()
                val formDetailsState = formState as NoteDetailState.FormDetails
                assertThat(formDetailsState.isSaveButtonEnabled).isFalse()
            }

            @Test
            fun `if form equals initial state, then emits new form state with save button disabled`() {
                val formChangedIntent = NoteDetailIntent.OnTextInput(
                    title = initialFormData?.title,
                    description = initialFormData?.description,
                    imageUrl = initialFormData?.description,
                )

                viewModel.onIntent(formChangedIntent)
                val formState = viewModel.formState.value

                assertThat(formState).isNotNull()
                assertThat(formState is NoteDetailState.FormDetails).isTrue()
                val formDetailsState = formState as NoteDetailState.FormDetails
                assertThat(formDetailsState.isSaveButtonEnabled).isFalse()
            }
        }
    }

    @Nested
    @DisplayName("Given noteId is not null")
    inner class NoteIdNotNull {
        private val noteId = 1L

        @Nested
        @DisplayName("When ViewModel is created")
        inner class Init {
            private val note = Note(
                id = noteId,
                title = "title",
                description = "I'm the note",
                createdAtDate = Date(),
                editedAtDate = Date(),
                imageUrl = IMAGE_URL,
            )

            @Test
            fun `then getNoteUseCase is called with the note id`() = runTest {
                getViewModel(noteId)

                verify(getNoteUseCase).invoke(noteId)
            }

            @Test
            fun `then formDataMapper is called with the getNoteUseCase result`() = runTest {
                whenever(getNoteUseCase(noteId)).thenReturn(
                    Response.Success(note)
                )
                getViewModel(noteId)

                verify(formDataMapper).mapNoteToFormData(note)
            }

            @Test
            fun `then FormState is emitted with filled form data`() = runTest {
                val expectedFormData = FormData(
                    id = note.id,
                    title = note.title,
                    description = note.description,
                    imageUrl = note.imageUrl,
                )
                whenever(getNoteUseCase(noteId)).thenReturn(
                    Response.Success(note)
                )
                whenever(formDataMapper.mapNoteToFormData(note)).thenReturn(expectedFormData)

                val viewModel = getViewModel(noteId)
                val formState = viewModel.formState.value

                assertThat(formState).isNotNull()
                assertThat(formState is NoteDetailState.FormDetails).isTrue()
                val formDetailsState = formState as NoteDetailState.FormDetails
                assertThat(formDetailsState.formData).isEqualTo(expectedFormData)
            }

            @Test
            fun `then FormState is emitted with delete button enabled`() = runTest {
                val expectedFormData = FormData(
                    id = note.id,
                    title = note.title,
                    description = note.description,
                    imageUrl = note.imageUrl,
                )
                whenever(getNoteUseCase(noteId)).thenReturn(
                    Response.Success(note)
                )
                whenever(formDataMapper.mapNoteToFormData(note)).thenReturn(expectedFormData)

                val viewModel = getViewModel(noteId)
                val formState = viewModel.formState.value

                assertThat(formState).isNotNull()
                assertThat(formState is NoteDetailState.FormDetails).isTrue()
                val formDetailsState = formState as NoteDetailState.FormDetails
                assertThat(formDetailsState.isDeleteButtonEnabled).isTrue()
            }
        }

        @Nested
        @DisplayName("When delete intent is received")
        inner class OnDeleteIntent {
            @Test
            fun `then delete use case is called with the noteId`() = runTest {
                val viewModel = getViewModel(noteId)

                viewModel.onIntent(NoteDetailIntent.Delete)

                verify(deleteNoteUseCase).invoke(noteId)
            }

            @Test
            fun `then NoteDeleted state is emitted`() = runTest {
                val viewModel = getViewModel(noteId)

                viewModel.onIntent(NoteDetailIntent.Delete)
                val formState = viewModel.formState.value

                assertThat(formState is NoteDetailState.NoteDeleted).isTrue()
            }
        }
    }

    private fun getViewModel(noteId: Long? = null) = NoteDetailViewModel(
        noteId = noteId,
        getNoteUseCase = getNoteUseCase,
        saveNoteUseCase = saveNoteUseCase,
        deleteNoteUseCase = deleteNoteUseCase,
        formDataMapper = formDataMapper,
    )
}
