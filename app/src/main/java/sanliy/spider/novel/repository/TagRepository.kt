package sanliy.spider.novel.repository

import io.ktor.client.call.body
import kotlinx.coroutines.flow.Flow
import sanliy.spider.novel.model.NovelPlatform
import sanliy.spider.novel.net.sfacg.ResponseSysTag
import sanliy.spider.novel.net.sfacg.SfacgService
import sanliy.spider.novel.room.TagDao
import sanliy.spider.novel.room.model.Tag
import javax.inject.Inject

class TagRepository @Inject constructor(
    private val tagDao: TagDao,
    private val sfacgService: SfacgService,
) {
    fun getWithSfacgAndID(vararg tagID: String): Flow<List<Tag>> {
        return tagDao.getWithId(*tagID, platform = NovelPlatform.SFACG)
    }

    suspend fun getWithSfacgAndName(tagName: String): String {
        return tagDao.getWithName(tagName, NovelPlatform.SFACG).tagID
    }

    fun getTags(platform: NovelPlatform): Flow<List<Tag>> {
        return tagDao.getWithPlatform(platform)
    }

    suspend fun updateTag(tag: Tag) {
        tagDao.update(tag)
    }

    suspend fun insertTag(tag: Tag) {
        tagDao.insert(tag)
    }

    suspend fun getSysTags(categoryId: Int): Result<ResponseSysTag> = runCatching {
        sfacgService.getSysTags(categoryId).body()
    }
}