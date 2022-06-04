package nl.com.lucianoluzzi.notes.domain.model

data class NoteUiModel(
    val id: Long? = null,
    val title: String,
    val description: String,
    val createdAtDate: String,
    val isEdited: Boolean = false,
    val imageUrl: String? = null
)
