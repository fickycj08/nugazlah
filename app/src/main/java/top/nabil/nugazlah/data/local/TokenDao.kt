package top.nabil.nugazlah.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface TokenDao {
    @Query("SELECT * FROM Token LIMIT 1")
    suspend fun get(): Token

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(token: Token)

    @Query("DELETE FROM Token")
    suspend fun delete()
}