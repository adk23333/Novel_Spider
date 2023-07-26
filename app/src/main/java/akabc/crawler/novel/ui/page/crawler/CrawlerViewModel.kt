package akabc.crawler.novel.ui.page.crawler

import akabc.crawler.novel.model.DbSfNovel
import akabc.crawler.novel.model.Task
import akabc.crawler.novel.net.sfacg.api.SfacgAPI
import akabc.crawler.novel.net.sfacg.model.ResultSysTag
import akabc.crawler.novel.room.NovelsDatabase
import akabc.crawler.novel.share.writeToExcelAndShare
import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.withContext

class CrawlerViewModel(private val db: NovelsDatabase) : ViewModel() {
    private val retrofit = SfacgAPI.createSfacgAPI()
    private val gson = Gson()
    var task by mutableStateOf(Task(null))
    val export = mutableStateOf(false)

    val novelsStateFlow = MutableStateFlow("准备开始...")

    fun getNovels(context: Context) {
        val mutex = Mutex()
        var page = 1
        var blankData = 0       //记录返回的空数据数量
        viewModelScope.launch {
            while (blankData == 0) {
                val jobs = mutableListOf<Job>()
                repeat(100) {
                    val job = CoroutineScope(Dispatchers.IO).launch {
                        novelsStateFlow.runCatching {
                            var map: Map<String, String>
                            mutex.withLock {
                                map = task.toMap(page)
                                page++
                            }
                            retrofit.getNovels(task.base.requestNovels.type, map)

                        }.onSuccess {
                            if (it.isSuccessful) {
                                val data = it.body()!!.data
                                mutex.withLock {
                                    Log.d(this@CrawlerViewModel::class.simpleName, data.toString())
                                }

                                if (data.isEmpty()) {
                                    mutex.withLock {
                                        blankData++
                                    }
                                } else {
                                    val newData = mutableListOf<DbSfNovel>()
                                    var showData = ""

                                    data.forEach { novel ->
                                        newData.add(DbSfNovel(task.base.id!!, novel))
                                        showData =
                                            "$showData${novel.novelName}  BY： ${novel.authorName}\n"
                                    }
                                    db.sfNovelsDao().insertAll(newData)
                                    mutex.withLock {
                                        if (novelsStateFlow.value.length > 10000) {
                                            novelsStateFlow.value =
                                                novelsStateFlow.value.substring(5000)
                                        }
                                        novelsStateFlow.emit("${novelsStateFlow.value}已爬取以下书籍：\n$showData")
                                    }
                                }
                            } else {
                                val errorBody = it.errorBody()?.string()
                                val error = gson.fromJson(errorBody, ResultSysTag::class.java)
                                mutex.withLock {
                                    withContext(Dispatchers.Main) {
                                        Toast.makeText(
                                            context,
                                            "${error.status.httpCode},${error.status.errorCode}:${error.status.msg}",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }
                                    Log.d(
                                        this@CrawlerViewModel::class.simpleName,
                                        errorBody.toString()
                                    )
                                }
                            }
                        }.onFailure {
                            mutex.withLock {
                                withContext(Dispatchers.Main) {
                                    Toast.makeText(context, it.message, Toast.LENGTH_SHORT).show()
                                }
                                Log.d(
                                    this@CrawlerViewModel::class.simpleName,
                                    it.message.toString()
                                )
                            }
                        }
                    }
                    jobs.add(job)
                }
                jobs.forEach {
                    it.join()
                }
            }
            export.value = true
        }

    }

    fun insertTask() {
        viewModelScope.launch(Dispatchers.IO) {
            task.base.id = db.taskDao().insertTaskBase(task.base)

            task.systagids.forEachIndexed { index, _ ->
                task.systagids[index].sysTagTaskId = task.base.id!!
            }
            task.notexcludesystagids.forEachIndexed { index, _ ->
                task.notexcludesystagids[index].notexcludesystagTaskId = task.base.id!!
            }
            Log.d(this::class.simpleName, task.toString())
            db.taskDao().insertSysTag(task.systagids)
            db.taskDao().insertSysTag(task.notexcludesystagids)
        }
    }

    fun shareToExcel(context: Context){
        viewModelScope.launch(Dispatchers.IO) {
            val novels = db.sfNovelsDao().getTaskNovels(task.base.id!!)
            writeToExcelAndShare(task, context, novels)
        }
    }
}

class CrawlerViewModelFactory(private val db: NovelsDatabase) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CrawlerViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return CrawlerViewModel(db) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}