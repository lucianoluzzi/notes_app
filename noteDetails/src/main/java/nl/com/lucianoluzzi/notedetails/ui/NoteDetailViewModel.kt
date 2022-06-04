package nl.com.lucianoluzzi.notedetails.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import nl.com.lucianoluzzi.domain.model.Response
import nl.com.lucianoluzzi.domain.useCase.DeleteNoteUseCase
import nl.com.lucianoluzzi.domain.useCase.GetNoteUseCase
import nl.com.lucianoluzzi.domain.useCase.SaveNoteUseCase
import nl.com.lucianoluzzi.notedetails.domain.FormDataMapper
import nl.com.lucianoluzzi.notedetails.domain.model.FormData
import nl.com.lucianoluzzi.notedetails.domain.model.NoteDetailIntent
import nl.com.lucianoluzzi.notedetails.domain.model.NoteDetailState

class NoteDetailViewModel(
    private val noteId: Long?,
    private val getNoteUseCase: GetNoteUseCase,
    private val saveNoteUseCase: SaveNoteUseCase,
    private val deleteNoteUseCase: DeleteNoteUseCase,
    private val formDataMapper: FormDataMapper,
) : ViewModel() {
    private var initialFormData: FormData? = null
    private val isDeleteButtonEnabled: Boolean
        get() = initialFormData != null

    private val _formState = MutableLiveData<NoteDetailState>()
    val formState: LiveData<NoteDetailState> = _formState

    init {
        viewModelScope.launch {
            initialFormData = noteId?.let {
                when (val response = getNoteUseCase(noteId)) {
                    is Response.Error -> null
                    is Response.Success -> {
                        response.data?.let {
                            formDataMapper.mapNoteToFormData(it)
                        }
                    }
                }
            }
            _formState.value = NoteDetailState.FormDetails(
                formData = initialFormData,
                isDeleteButtonEnabled = isDeleteButtonEnabled,
            )
        }
    }

    fun onIntent(intent: NoteDetailIntent) {
        when (intent) {
            NoteDetailIntent.Delete -> deleteNote()
            is NoteDetailIntent.Save -> saveNote(intent)
            is NoteDetailIntent.OnTextInput -> {
                if (isFormChanged(intent)) {
                    _formState.value = NoteDetailState.FormDetails(
                        isSaveButtonEnabled = intent.title?.isNotEmpty() == true &&
                            intent.description?.isNotEmpty() == true,
                        isDeleteButtonEnabled = isDeleteButtonEnabled,
                    )
                } else {
                    _formState.value = NoteDetailState.FormDetails(
                        isSaveButtonEnabled = false,
                        isDeleteButtonEnabled = isDeleteButtonEnabled,
                    )
                }
            }
        }
    }

    private fun saveNote(intent: NoteDetailIntent.Save) {
        viewModelScope.launch {
            saveNoteUseCase(
                noteId = noteId,
                title = intent.title,
                description = intent.description,
                imageUrl = intent.imageUrl,
            )
            _formState.value = NoteDetailState.NoteSaved
        }
    }

    private fun deleteNote() {
        viewModelScope.launch {
            noteId?.let {
                deleteNoteUseCase(it)
                _formState.value = NoteDetailState.NoteDeleted
            }
        }
    }

    private fun isFormChanged(textInputIntent: NoteDetailIntent.OnTextInput): Boolean {
        textInputIntent.title?.let {
            if (it != initialFormData?.title) {
                return true
            }
        }
        textInputIntent.description?.let {
            if (it != initialFormData?.description) {
                return true
            }
        }
        textInputIntent.imageUrl?.let {
            if (it != initialFormData?.imageUrl) {
                return true
            }
        }

        return false
    }
}
