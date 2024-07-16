package sanliy.spider.novel.room

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import sanliy.spider.novel.room.model.SfacgNovelImpl

@Dao
interface SfacgNovelDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(vararg novels: SfacgNovelImpl)

    @Delete
    suspend fun delete(novel: SfacgNovelImpl)

    @Query("DELETE FROM sfacg_novels WHERE parent_task_id = :taskID")
    suspend fun deleteForTask(taskID: Long)


    @Query("SELECT * FROM sfacg_novels WHERE parent_task_id = :taskID")
    suspend fun getWithTask(taskID: Long): List<SfacgNovelImpl>
}