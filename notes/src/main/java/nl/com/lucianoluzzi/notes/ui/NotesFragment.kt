package nl.com.lucianoluzzi.notes.ui

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import nl.com.lucianoluzzi.design.extensions.doOnSearchAction
import nl.com.lucianoluzzi.design.extensions.dp
import nl.com.lucianoluzzi.design.extensions.viewBinding
import nl.com.lucianoluzzi.navigation.Navigator
import nl.com.lucianoluzzi.notes.R
import nl.com.lucianoluzzi.notes.databinding.FragmentNotesBinding
import nl.com.lucianoluzzi.notes.domain.model.NotesState
import nl.com.lucianoluzzi.notes.ui.noteList.GridSpacingItemDecoration
import nl.com.lucianoluzzi.notes.ui.noteList.NoteAdapter

class NotesFragment(
    private val viewModel: NotesViewModel,
    private val navigator: Navigator
) : Fragment(R.layout.fragment_notes) {
    private val viewBinding by viewBinding<FragmentNotesBinding>()
    private val noteAdapter = NoteAdapter { noteClicked ->
        viewModel.onNoteClick(noteClicked)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setNoteListLayout()
        setCreateClick()

        viewModel.notesState.observe(viewLifecycleOwner) {
            handleNotesState(it)
        }
    }

    private fun setCreateClick() {
        viewBinding.create.setOnClickListener {
            viewModel.onCreateClick()
        }
    }

    private fun handleNotesState(notesState: NotesState) {
        when (notesState) {
            NotesState.Error -> showError()
            is NotesState.NotesList -> handleNotesListState(notesState)
        }
    }

    private fun setNoteListLayout() = with(viewBinding) {
        notes.adapter = noteAdapter
        notes.layoutManager = StaggeredGridLayoutManager(
            2,
            StaggeredGridLayoutManager.VERTICAL
        )
        if (notes.itemDecorationCount == 0) {
            notes.addItemDecoration(GridSpacingItemDecoration(8.dp))
        }

        search.doOnSearchAction { searchText ->
            viewModel.fetchNotes(searchText)
        }

    }

    private fun handleNotesListState(notesListState: NotesState.NotesList) {
        viewBinding.errorMessage.isVisible = false
        noteAdapter.submitList(notesListState.notes)
        if (notesListState.showNoteDetail != null) {
            viewModel.hasNavigatedToNoteDetail()
            navigator.navigateToNoteDetail(
                noteId = notesListState.showNoteDetail.noteId.toString(),
                navigationController = findNavController()
            )
        } else if (notesListState.createNewNote) {
            viewModel.hasNavigatedToNoteDetail()
            navigator.navigateToNoteDetail(
                navigationController = findNavController()
            )
        }
    }

    private fun showError() = with(viewBinding) {
        errorMessage.isVisible = true
        errorMessage.text = getString(R.string.notes_error_message)
    }
}
