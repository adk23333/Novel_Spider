package sanliy.spider.novel.repository

import kotlinx.coroutines.flow.Flow
import sanliy.spider.novel.room.GenreDao
import sanliy.spider.novel.room.SfacgTaskDao
import sanliy.spider.novel.room.TagDao
import sanliy.spider.novel.room.model.SfacgNovelListTaskImpl
import sanliy.spider.novel.room.model.TagImpl
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TaskRepository @Inject constructor(
    private val sfacgTaskDao: SfacgTaskDao,
    private val tagDao: TagDao,
    private val genreDao: GenreDao,
) {

    fun insertSfacgNLT(task: SfacgNovelListTaskImpl): Long {
        return sfacgTaskDao.insert(task)
    }

    fun insertTag(tags: List<TagImpl>) {
        tagDao.insert(*tags.toTypedArray())
    }

    fun updateTask(task: SfacgNovelListTaskImpl) {
        sfacgTaskDao.update(task)
    }


    fun deleteExpiredTasks() {
        sfacgTaskDao.deleteExpiredTasks()
    }


    suspend fun deleteTags(tag: TagImpl) {
        tagDao.delete(tag)
    }


    fun getTasks(): Flow<List<SfacgNovelListTaskImpl>> {
        return sfacgTaskDao.getTasks()
    }

    fun getMarkedTasks(): Flow<List<SfacgNovelListTaskImpl>> {
        return sfacgTaskDao.getMarkedTasks()
    }

    fun getTaskByID(taskID: Long): Flow<SfacgNovelListTaskImpl?> {
        return sfacgTaskDao.getTaskById(taskID)
    }
}