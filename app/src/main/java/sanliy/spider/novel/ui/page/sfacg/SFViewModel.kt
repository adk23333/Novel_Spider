package sanliy.spider.novel.ui.page.sfacg

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json
import sanliy.spider.novel.model.Task
import sanliy.spider.novel.net.sfacg.api.SfacgAPI
import sanliy.spider.novel.net.sfacg.model.ResultSysTag
import sanliy.spider.novel.net.sfacg.model.SysTag
import sanliy.spider.novel.room.NovelsDatabase
import javax.inject.Inject

@HiltViewModel
class SFViewModel @Inject constructor(val db: NovelsDatabase) : ViewModel() {
    private val retrofit = SfacgAPI.createSfacgAPI()

    var isCrawlerContext by mutableStateOf(false)
    var refreshTags by mutableStateOf(false)
    var task by mutableStateOf(Task(null))

    var tagsStateFlow = MutableStateFlow<List<SysTag>>(listOf())

    fun getSysTags(context: Context) {
        viewModelScope.launch(Dispatchers.IO) {
            refreshTags = true
            tagsStateFlow.runCatching {
                retrofit.getSysTags(0)
            }.onSuccess {
                if (it.isSuccessful) {
                    tagsStateFlow.emit(it.body()!!.data)
                } else {
                    it.errorBody()?.let { responseBody ->
                        val error = Json.decodeFromString<ResultSysTag>(responseBody.toString())
                        withContext(Dispatchers.Main) {
                            Toast.makeText(
                                context,
                                "${error.status.httpCode},${error.status.errorCode}:${error.status.msg}",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                        Log.d(this@SFViewModel::class.simpleName, responseBody.toString())
                    }
                }
            }.onFailure {
                withContext(Dispatchers.Main) {
                    Toast.makeText(context, it.message, Toast.LENGTH_SHORT).show()
                }
                Log.w(this@SFViewModel::class.simpleName, it.toString())
            }
            refreshTags = false
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


}
