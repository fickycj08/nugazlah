package top.nabil.nugazlah.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface TaskDao {
    @Query("SELECT * FROM Task WHERE classId = :classId")
    suspend fun getByClassId(classId: String): List<Task>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun batchInsert(tasks: List<Task>)

    @Query("UPDATE Task SET isTaskDone = 'true' WHERE taskId = :taskId")
    suspend fun setTaskToDone(taskId: String)
}
