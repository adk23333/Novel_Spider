package sanliy.spider.novel.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import kotlinx.coroutines.flow.Flow
import sanliy.spider.novel.model.Task
import sanliy.spider.novel.net.sfacg.model.SysTag

@Dao
interface TaskDao {

    @Insert
    fun insertTaskBase(base: Task.Base): Long

    @Insert
    fun insertSysTag(sysTag: List<SysTag>)

    @Update
    fun updateTask(base: Task.Base)

    @Query("DELETE FROM task WHERE isDelete == 1 AND isMark == 0")
    fun deleteTasks()

    @Query("DELETE FROM SysTag WHERE sysTagTaskId == :taskId OR notexcludesystagTaskId==:taskId")
    fun deleteSysTags(taskId: Long)

    @Query("SELECT id FROM task WHERE isDelete == 1 AND isMark == 0")
    suspend fun getDeleteTasksId(): List<Long>

    @Transaction
    @Query("SELECT * FROM task WHERE isDelete == 0")
    fun getTasks(): Flow<List<Task>>

    @Transaction
    @Query("SELECT * FROM task WHERE isMark == 1")
    fun getMarkTasks(): Flow<List<Task>>
}