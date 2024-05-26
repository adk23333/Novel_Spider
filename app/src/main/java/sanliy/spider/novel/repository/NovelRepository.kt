package sanliy.spider.novel.repository

import sanliy.spider.novel.model.DbSfNovel
import sanliy.spider.novel.model.Task
import sanliy.spider.novel.room.SfNovelsDao
import javax.inject.Inject


class NovelRepository @Inject constructor(private val novelDao: SfNovelsDao) {

    suspend fun insert(vararg novels: DbSfNovel) {
        novelDao.insert(*novels)
    }

    suspend fun deleteWithTask(task: Task) {
        novelDao.delete(task.base.id!!)
    }

    suspend fun getWithTask(task: Task): List<DbSfNovel> {
        return novelDao.getTaskNovels(task.base.id!!)
    }
}