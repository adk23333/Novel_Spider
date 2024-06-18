package sanliy.spider.novel.room

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow
import sanliy.spider.novel.model.NovelPlatform
import sanliy.spider.novel.room.model.Genre

@Dao
interface GenreDao {
    @Query("SELECT * FROM genres WHERE platform = :platform")
    fun getWithPlatform(platform: NovelPlatform): Flow<List<Genre>>

    @Query("SELECT * FROM genres WHERE genre_id = :genreID AND platform = :platform")
    fun getSfacgGenreWithId(genreID: String, platform: NovelPlatform): Flow<Genre>

    @Delete
    fun delete(genre: Genre)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(vararg genre: Genre)

    @Update
    fun update(genre: Genre)

}