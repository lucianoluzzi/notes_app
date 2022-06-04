package nl.com.lucianoluzzi.notedetails.domain.model

sealed class NoteDetailState {
    object NoteSaved : NoteDetailState()
    object NoteDeleted : NoteDetailState()
    data class FormDetails(
        val formData: FormData? = null,
        val isSaveButtonEnabled: Boolean = false,
        val isDeleteButtonEnabled: Boolean = false,
    ) : NoteDetailState()
}
