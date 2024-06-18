package sanliy.spider.novel.room

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow
import sanliy.spider.novel.model.NovelPlatform
import sanliy.spider.novel.room.model.Tag

@Dao
interface TagDao {
    @Query("SELECT * FROM tags WHERE platform = :platform")
    fun getWithPlatform(platform: NovelPlatform): Flow<List<Tag>>

    @Query("SELECT * FROM tags WHERE tag_id IN (:tagID) AND platform = :platform")
    fun getWithId(vararg tagID: String, platform: NovelPlatform): Flow<List<Tag>>

    @Query("SELECT * FROM tags WHERE tag_name = :tagName AND platform = :platform")
    fun getWithName(tagName: String, platform: NovelPlatform): Tag


    @Delete
    suspend fun delete(tag: Tag)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(vararg tag: Tag)

    @Update
    suspend fun update(tag: Tag)

}