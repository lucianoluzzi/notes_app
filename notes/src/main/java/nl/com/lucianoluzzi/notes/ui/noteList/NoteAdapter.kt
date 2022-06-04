package nl.com.lucianoluzzi.notes.ui.noteList

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import nl.com.lucianoluzzi.notes.R
import nl.com.lucianoluzzi.notes.databinding.NoteItemBinding
import nl.com.lucianoluzzi.notes.domain.model.NoteUiModel

class NoteAdapter(
    private val onNoteClick: (Long) -> Unit
) : ListAdapter<NoteUiModel, NoteAdapter.NoteViewHolder>(noteDiffUtil) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val noteItemBinding = NoteItemBinding.inflate(layoutInflater, parent, false)
        return NoteViewHolder(noteItemBinding)
    }

    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {
        val note = getItem(position)
        holder.setViews(note)
    }

    inner class NoteViewHolder(
        private val noteItemBinding: NoteItemBinding
    ) : RecyclerView.ViewHolder(noteItemBinding.root) {

        fun setViews(note: NoteUiModel) = with(noteItemBinding) {
            title.text = note.title
            description.text = note.description
            createdTag.text = note.createdAtDate
            editedTag.isVisible = note.isEdited
            setImage(note)
            root.setOnClickListener {
                note.id?.let {
                    onNoteClick(it)
                }
            }
            setAccessibilityLabel(note.title)
        }

        private fun setAccessibilityLabel(title: String) {
            val itemPosition = (layoutPosition + 1).toString()
            val contentDescriptionText = noteItemBinding.root.context.getString(
                R.string.note_item_accessibility_label,
                itemPosition,
                title
            )
            noteItemBinding.root.contentDescription = contentDescriptionText
        }

        private fun NoteItemBinding.setImage(note: NoteUiModel) {
            image.isVisible = if (note.imageUrl == null) {
                false
            } else {
                image.load(note.imageUrl) {
                    crossfade(true)
                }
                true
            }
        }
    }

    private companion object {
        private val noteDiffUtil = object : DiffUtil.ItemCallback<NoteUiModel>() {
            override fun areItemsTheSame(oldItem: NoteUiModel, newItem: NoteUiModel) =
                oldItem == newItem

            override fun areContentsTheSame(oldItem: NoteUiModel, newItem: NoteUiModel) =
                oldItem == newItem
        }
    }
}
