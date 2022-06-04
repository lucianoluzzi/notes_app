package nl.com.lucianoluzzi.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.util.*

@Parcelize
data class Note(
    val id: Long? = null,
    val title: String,
    val description: String,
    val createdAtDate: Date,
    val editedAtDate: Date? = null,
    val imageUrl: String? = null
) : Parcelable
