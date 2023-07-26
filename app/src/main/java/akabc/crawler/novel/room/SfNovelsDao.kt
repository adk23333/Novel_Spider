package akabc.crawler.novel.room

import akabc.crawler.novel.model.DbSfNovel
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction

@Dao
interface SfNovelsDao {
    @Insert
    suspend fun insert(vararg novels: DbSfNovel)

    @Insert
    suspend fun insertAll(novels: List<DbSfNovel>)

    @Transaction
    @Query("DELETE FROM novels WHERE taskId == :taskId")
    suspend fun delete(taskId: Long)

    @Transaction
    @Query("SELECT * FROM Novels WHERE taskId == :taskId")
    suspend fun getTaskNovels(taskId: Long): List<DbSfNovel>
}