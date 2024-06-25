package sanliy.spider.novel.repository

import kotlinx.coroutines.flow.Flow
import sanliy.spider.novel.room.GenreDao
import sanliy.spider.novel.room.SfacgTaskDao
import sanliy.spider.novel.room.TagDao
import sanliy.spider.novel.room.model.SfacgNovelListTask
import sanliy.spider.novel.room.model.Tag
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TaskRepository @Inject constructor(
    private val sfacgTaskDao: SfacgTaskDao,
    private val tagDao: TagDao,
    private val genreDao: GenreDao,
) {

    fun insertSfacgNLT(task: SfacgNovelListTask): Long {
        return sfacgTaskDao.insert(task)
    }

    fun insertTag(tags: List<Tag>) {
        tagDao.insert(*tags.toTypedArray())
    }

    fun updateTask(task: SfacgNovelListTask) {
        sfacgTaskDao.update(task)
    }


    fun deleteExpiredTasks() {
        sfacgTaskDao.deleteExpiredTasks()
    }


    suspend fun deleteTags(tag: Tag) {
        tagDao.delete(tag)
    }


    fun getTasks(): Flow<List<SfacgNovelListTask>> {
        return sfacgTaskDao.getTasks()
    }

    fun getMarkedTasks(): Flow<List<SfacgNovelListTask>> {
        return sfacgTaskDao.getMarkedTasks()
    }

    fun getTaskByID(taskID: Long): Flow<SfacgNovelListTask?> {
        return sfacgTaskDao.getTaskById(taskID)
    }
}