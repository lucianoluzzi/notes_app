package nl.com.lucianoluzzi.navigationimplementation

import androidx.navigation.NavController
import nl.com.lucianoluzzi.navigation.Navigator
import nl.com.lucianoluzzi.notes.ui.NotesFragmentDirections

class NavigatorImpl : Navigator {

    override fun navigateToNoteDetail(
        noteId: String?,
        navigationController: NavController
    ) {
        val toNoteDetailFragment =
            NotesFragmentDirections.actionNotesFragmentToNoteDetailFragment(noteId)
        navigationController.navigate(toNoteDetailFragment)
    }
}
