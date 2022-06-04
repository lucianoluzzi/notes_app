package nl.com.lucianoluzzi.data.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity(tableName = "note")
data class NoteEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val title: String,
    val description: String,
    @ColumnInfo(name = "created_at") val createdAt: Date,
    @ColumnInfo(name = "edited_at") val editedAt: Date? = null,
    @ColumnInfo(name = "image_url") val imageUrl: String? = null
)
