package sanliy.spider.novel.repository

import io.ktor.client.call.body
import sanliy.spider.novel.net.sfacg.ResponseNovels
import sanliy.spider.novel.net.sfacg.SfacgService
import sanliy.spider.novel.room.SfacgNovelDao
import sanliy.spider.novel.room.model.SfacgNovel
import sanliy.spider.novel.room.model.SfacgNovelListTask
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class NovelRepository @Inject constructor(
    private val sfacgNovelDao: SfacgNovelDao,
    private val sfacgService: SfacgService,
) {

    suspend fun insertSfacg(vararg novels: SfacgNovel) {
        sfacgNovelDao.insert(*novels)
    }

    suspend fun deleteSfacgForTask(task: SfacgNovelListTask) {
        sfacgNovelDao.deleteForTask(task.taskID!!)
    }

    suspend fun getSfacgWithTask(task: SfacgNovelListTask): List<SfacgNovel> {
        return sfacgNovelDao.getWithTask(task.taskID!!)
    }

    suspend fun getNovels(novelsType: Int, task: SfacgNovelListTask): ResponseNovels {
        return sfacgService.getNovels(novelsType, task).body()
    }
}