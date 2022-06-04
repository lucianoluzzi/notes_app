package nl.com.lucianoluzzi.notedetails.domain.model

sealed class NoteDetailIntent {
    object Delete : NoteDetailIntent()
    data class Save(
        val title: String,
        val description: String,
        val imageUrl: String? = null,
    ) : NoteDetailIntent()
    data class OnTextInput(
        val title: String? = null,
        val description: String? = null,
        val imageUrl: String? = null,
    ) : NoteDetailIntent()
}
