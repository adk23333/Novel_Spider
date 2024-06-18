package sanliy.spider.novel.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow
import sanliy.spider.novel.room.model.SfacgNovelListTask

@Dao
interface SfacgTaskDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(task: SfacgNovelListTask): Long

    @Update
    fun update(task: SfacgNovelListTask)

    @Query("DELETE FROM sfacg_novel_list_task WHERE is_delete == 1 AND is_mark == 0")
    fun deleteExpiredTasks()

    @Query("SELECT * FROM sfacg_novel_list_task WHERE is_delete == 0")
    fun getTasks(): Flow<List<SfacgNovelListTask>>

    @Query("SELECT * FROM sfacg_novel_list_task WHERE is_mark == 1")
    fun getMarkedTasks(): Flow<List<SfacgNovelListTask>>

    @Query("SELECT * FROM sfacg_novel_list_task WHERE task_id = :id")
    fun getTaskById(id: Long): Flow<SfacgNovelListTask?>

}