package akabc.crawler.novel.ui.page.sfacg

import akabc.crawler.novel.model.Task
import akabc.crawler.novel.net.sfacg.api.SfacgAPI
import akabc.crawler.novel.net.sfacg.model.ResultSysTag
import akabc.crawler.novel.net.sfacg.model.SysTag
import akabc.crawler.novel.room.NovelsDatabase
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
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class SFViewModel(private val db: NovelsDatabase) : ViewModel() {
    private val retrofit = SfacgAPI.createSfacgAPI()
    private val gson = Gson()

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
                    val errorBody = it.errorBody()?.string()
                    val error = gson.fromJson(errorBody, ResultSysTag::class.java)
                    Toast.makeText(
                        context,
                        "${error.status.httpCode},${error.status.errorCode}:${error.status.msg}",
                        Toast.LENGTH_SHORT
                    ).show()
                    Log.d(this@SFViewModel::class.simpleName, errorBody.toString())
                }
            }.onFailure {
                Toast.makeText(context, it.message, Toast.LENGTH_SHORT).show()
                Log.d(this@SFViewModel::class.simpleName, it.message.toString())
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

class SFViewModelFactory(private val db: NovelsDatabase) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SFViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return SFViewModel(db) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
