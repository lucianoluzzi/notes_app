package nl.com.lucianoluzzi.notedetails.ui

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import nl.com.lucianoluzzi.design.extensions.doIfTextChanged
import nl.com.lucianoluzzi.design.extensions.getTextOrNull
import nl.com.lucianoluzzi.design.extensions.viewBinding
import nl.com.lucianoluzzi.navigation.Navigator.Companion.NOTE_ARGUMENT_KEY
import nl.com.lucianoluzzi.notedetails.R
import nl.com.lucianoluzzi.notedetails.databinding.FragmentNoteDetailBinding
import nl.com.lucianoluzzi.notedetails.domain.model.FormData
import nl.com.lucianoluzzi.notedetails.domain.model.NoteDetailIntent
import nl.com.lucianoluzzi.notedetails.domain.model.NoteDetailState
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class NoteDetailFragment : Fragment(R.layout.fragment_note_detail) {
    private val viewBinding by viewBinding<FragmentNoteDetailBinding>()
    private val noteId: String? by lazy {
        requireArguments().getString(NOTE_ARGUMENT_KEY)
    }
    private val viewModel by viewModel<NoteDetailViewModel> {
        parametersOf(noteId?.toLong())
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setButtons()
        setTextListeners()
        viewModel.formState.observe(viewLifecycleOwner) { formState ->
            handleState(formState)
        }
    }

    private fun setButtons() = with(viewBinding) {
        toolbar.setNavigationOnClickListener {
            findNavController().popBackStack()
        }
        delete.isVisible = noteId != null
        delete.setOnClickListener {
            viewModel.onIntent(NoteDetailIntent.Delete)
        }
        save.setOnClickListener {
            viewModel.onIntent(getSaveIntent())
        }
    }

    private fun setTextListeners() = with(viewBinding) {
        title.doIfTextChanged { text ->
            viewModel.onIntent(
                NoteDetailIntent.OnTextInput(
                    title = text,
                    description = description.getTextOrNull(),
                    imageUrl = imageUrl.getTextOrNull(),
                )
            )
        }
        description.doIfTextChanged { text ->
            viewModel.onIntent(
                NoteDetailIntent.OnTextInput(
                    title = title.getTextOrNull(),
                    description = text,
                    imageUrl = imageUrl.getTextOrNull(),
                )
            )
        }
        imageUrl.doIfTextChanged { text ->
            viewModel.onIntent(
                NoteDetailIntent.OnTextInput(
                    title = title.getTextOrNull(),
                    description = description.getTextOrNull(),
                    imageUrl = text,
                )
            )
        }
    }

    private fun getSaveIntent() = NoteDetailIntent.Save(
        title = viewBinding.title.text.toString(),
        description = viewBinding.description.text.toString(),
        imageUrl = viewBinding.imageUrl.getTextOrNull(),
    )

    private fun handleState(formState: NoteDetailState) {
        when (formState) {
            is NoteDetailState.FormDetails -> handleFormDetails(formState)
            NoteDetailState.NoteDeleted -> findNavController().popBackStack()
            NoteDetailState.NoteSaved -> findNavController().popBackStack()
        }
    }

    private fun handleFormDetails(formDetails: NoteDetailState.FormDetails) = with(viewBinding) {
        save.isVisible = formDetails.isSaveButtonEnabled
        delete.isVisible = formDetails.isDeleteButtonEnabled
        formDetails.formData?.let {
            fillFormData(it)
        }
    }

    private fun fillFormData(formData: FormData) = with(viewBinding) {
        title.setText(formData.title)
        description.setText(formData.description)
        imageUrl.setText(formData.imageUrl)
    }
}
