package nl.com.lucianoluzzi.notes.domain.model

data class ShowNoteDetail(
    val noteId: Long? = null
)

sealed class NotesState {
    object Error : NotesState()
    data class NotesList(
        val notes: List<NoteUiModel>,
        val showNoteDetail: ShowNoteDetail? = null,
        val createNewNote: Boolean = false
    ) : NotesState()
}
