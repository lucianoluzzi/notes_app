package nl.com.lucianoluzzi.notes.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import nl.com.lucianoluzzi.domain.model.Response
import nl.com.lucianoluzzi.domain.useCase.GetNotesUseCase
import nl.com.lucianoluzzi.notes.domain.NoteUiMapper
import nl.com.lucianoluzzi.notes.domain.model.NotesState
import nl.com.lucianoluzzi.notes.domain.model.ShowNoteDetail

class NotesViewModel(
    private val noteUiMapper: NoteUiMapper,
    private val getNotesUseCase: GetNotesUseCase,
) : ViewModel() {
    private val getCurrentNotesList: NotesState.NotesList?
        get() = if (_notesState.value is NotesState.NotesList) {
            _notesState.value as NotesState.NotesList
        } else {
            null
        }

    private val _notesState = MutableLiveData<NotesState>(
        NotesState.NotesList(notes = emptyList())
    )
    val notesState: LiveData<NotesState> = _notesState

    init {
        viewModelScope.launch {
            emitNotes()
        }
    }

    private suspend fun emitNotes() {
        when (val response = getNotesUseCase()) {
            is Response.Error -> _notesState.value = NotesState.Error
            is Response.Success -> {
                response.data?.collect { notes ->
                    val noteUiModelList = noteUiMapper.mapNotesToNoteUiModelList(notes)
                    _notesState.value = NotesState.NotesList(notes = noteUiModelList)
                }
            }
        }
    }

    fun onCreateClick() {
        getCurrentNotesList?.let {
            _notesState.value = it.copy(createNewNote = true)
        }
    }

    fun onNoteClick(noteId: Long) {
        viewModelScope.launch {
            getCurrentNotesList?.let { notesListState ->
                _notesState.value = notesListState.copy(
                    showNoteDetail = ShowNoteDetail(noteId)
                )
            }
        }
    }

    fun hasNavigatedToNoteDetail() {
        getCurrentNotesList?.let {
            _notesState.value = it.copy(
                createNewNote = false,
                showNoteDetail = null,
            )
        }
    }
}
