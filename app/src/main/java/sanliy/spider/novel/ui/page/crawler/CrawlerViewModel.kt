package sanliy.spider.novel.ui.page.crawler

import android.util.Log
import androidx.compose.foundation.ScrollState
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
import sanliy.spider.novel.repository.NovelRepository
import sanliy.spider.novel.repository.TaskRepository
import sanliy.spider.novel.room.model.SfacgNovel
import sanliy.spider.novel.room.model.SfacgNovelListTask
import sanliy.spider.novel.share.writeToExcelAndShare
import javax.inject.Inject

@HiltViewModel
class CrawlerViewModel @Inject constructor(
    private val novelRepository: NovelRepository,
    private val taskRepository: TaskRepository,
) : ViewModel() {
    private val mutex = Mutex()
    private var page = 1
    private var isLastPage = false       //记录返回的空数据数量
    var threadCount by mutableIntStateOf(2)

    var export by mutableStateOf(false)
    private val logSizeLimit = 10000
    var logSize by mutableIntStateOf(2)
    val logs = mutableStateListOf("准备开始...")
    var state = ScrollState(0)

    fun finishTask(task: SfacgNovelListTask) {
        viewModelScope.launch {
            val jobs = mutableListOf<Job>()
            repeat(threadCount) {
                val job = viewModelScope.launch(Dispatchers.IO) {
                    while (!isLastPage) {
                        runJob(task)
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

    private suspend fun runJob(task: SfacgNovelListTask) {
        mutex.withLock {
            page++
        }
        runCatching {
            novelRepository.getNovels(page, task)
        }.onSuccess {
            if (it.data.isEmpty()) {
                mutex.withLock {
                    isLastPage = true
                }
            } else {
                val novels = mutableListOf<SfacgNovel>()
                it.data.forEach { novel ->
                    novels.add(novel.toSfacgNovel(task))
                    mutex.withLock {
                        if (logs.size > logSizeLimit) {
                            logs.removeFirst()
                        }
                        logs.add("《${novel.novelName}》—— ${novel.authorName}")
                        logSize++
                    }
                }
                novelRepository.insertSfacg(*novels.toTypedArray())
            }
        }.onFailure {
            Log.d(this@CrawlerViewModel::class.simpleName, it.toString())
        }
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
}
