package nl.com.lucianoluzzi.navigation

import androidx.navigation.NavController

interface Navigator {
    fun navigateToNoteDetail(
        noteId: String? = null,
        navigationController: NavController
    )

    companion object {
        const val NOTE_ARGUMENT_KEY = "noteId"
    }
}
