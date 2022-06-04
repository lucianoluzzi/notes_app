package nl.com.lucianoluzzi.data.dao

import androidx.room.*
import kotlinx.coroutines.flow.Flow
import nl.com.lucianoluzzi.data.entity.NoteEntity
import java.util.*

@Dao
interface NoteDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(note: NoteEntity)

    @Throws(Exception::class)
    @Query("DELETE FROM note WHERE id = :noteId")
    suspend fun delete(noteId: Long)

    @Throws(Exception::class)
    @Update
    suspend fun update(note: NoteEntity)

    @Throws(Exception::class)
    @Transaction
    @Query("SELECT * FROM note")
    fun getNotes(): Flow<List<NoteEntity>>

    @Throws(Exception::class)
    @Query("SELECT * FROM note WHERE id = :id")
    suspend fun getNote(id: Long): NoteEntity?

    @Throws(Exception::class)
    @Query("SELECT created_at FROM note WHERE id = :id")
    suspend fun getCreationDate(id: Long): Date?
}
