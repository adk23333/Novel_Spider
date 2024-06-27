package sanliy.spider.novel.ui.page.crawler

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import sanliy.spider.novel.model.NovelPlatform
import sanliy.spider.novel.repository.GenreRepository
import sanliy.spider.novel.repository.NovelRepository
import sanliy.spider.novel.repository.TaskRepository
import sanliy.spider.novel.room.model.Genre
import sanliy.spider.novel.room.model.SfacgNovel
import sanliy.spider.novel.room.model.SfacgNovelListTask
import sanliy.spider.novel.room.model.Tag
import sanliy.spider.novel.share.writeToExcelAndShare
import java.time.LocalDateTime
import javax.inject.Inject

@HiltViewModel
class CrawlerViewModel @Inject constructor(
    private val novelRepository: NovelRepository,
    private val taskRepository: TaskRepository,
    private val genreRepository: GenreRepository,
) : ViewModel() {
    private val mutex = Mutex()
    private var page = 1
    var threadCount by mutableIntStateOf(2)

    var export by mutableStateOf(false)
    private val logSizeLimit = 10000
    var logSize by mutableIntStateOf(2)
    val logs = mutableStateListOf("准备开始...")


    fun finishTask(task: SfacgNovelListTask) {
        page = task.startPage
        viewModelScope.launch {
            val jobs = mutableListOf<Job>()
            repeat(threadCount) {
                val job = viewModelScope.launch(Dispatchers.IO) {
                    while (true) {
                        val isLastPage = runJob(task) ?: break
                        if (isLastPage) break
                    }
                }
                jobs.add(job)
            }
            jobs.forEach {
                it.join()
            }
            export = true
        }
    }

    private suspend fun runJob(task: SfacgNovelListTask): Boolean? {
        val mPage: Int
        mutex.withLock {
            mPage = page
            page++
        }
        if (mPage > task.endPage) return true
        return runCatching {
            novelRepository.getNovels(mPage, task)
        }.fold(
            onSuccess = {
                if (it.data.isEmpty()) {
                    true
                } else {
                    val novels = it.data.map { novel ->
                        val sfacgNovel = SfacgNovel(
                            novel.novelId.toString(),
                            task.taskID!!,
                            NovelPlatform.SFACG,
                            novel.novelName,
                            novel.expand.intro,
                            novel.authorId.toString(),
                            novel.authorName,
                            novel.expand.sysTags.map { sysTag ->
                                Tag(
                                    sysTag.sysTagId.toString(),
                                    NovelPlatform.SFACG,
                                    sysTag.tagName
                                )
                            },
                            LocalDateTime.parse(novel.lastUpdateTime),
                            novel.markCount,
                            novel.novelCover,
                            novel.bgBanner,
                            novel.point,
                            novel.isFinish,
                            novel.charCount,
                            novel.viewTimes,
                            novel.allowDown,
                            LocalDateTime.parse(novel.addTime),
                            novel.isSensitive,
                            novel.signStatus,
                            getGenre(novel.expand.typeName) ?: Genre.DefaultSFACG,
                            novel.categoryId
                        )
                        mutex.withLock {
                            if (logs.size > logSizeLimit) {
                                logs.removeFirst()
                            }
                            logs.add("《${novel.novelName}》—— ${novel.authorName}")
                            logSize++
                        }
                        sfacgNovel
                    }
                    novelRepository.insertSfacg(*novels.toTypedArray())
                    false
                }
            },
            onFailure = {
                Log.d(this@CrawlerViewModel::class.simpleName, it.toString())
                null
            }
        )
    }

    fun getTask(taskID: Long): Flow<SfacgNovelListTask> {
        return taskRepository.getTaskByID(taskID).filterNotNull()
    }

    fun shareToExcel(task: SfacgNovelListTask) {
        viewModelScope.launch(Dispatchers.IO) {
            val novels = novelRepository.getSfacgWithTask(task)
            writeToExcelAndShare(task, novels)
        }
    }

    suspend fun getGenre(name: String): Genre? {
        return genreRepository.getSfacgGenreByName(name)
    }
}
