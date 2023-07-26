package akabc.crawler.novel.ui.page.record

import akabc.crawler.novel.model.Task
import akabc.crawler.novel.room.NovelsDatabase
import akabc.crawler.novel.share.writeToExcelAndShare
import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class RecordViewModel(val db: NovelsDatabase) : ViewModel() {

    fun delete(task: Task) {
        viewModelScope.launch(Dispatchers.IO) {
            db.taskDao().updateTask(task.base.copy(isDelete = true))
            db.sfNovelsDao().delete(task.base.id!!)

            val ids = db.taskDao().getDeleteTasksId()
            ids.forEach {
                db.taskDao().deleteSysTags(it)
            }
            db.taskDao().deleteTasks()

        }
    }

    fun switchIsMark(task: Task) {
        viewModelScope.launch(Dispatchers.IO) {
            db.taskDao().updateTask(task.base.copy(isMark = !task.base.isMark))
        }
    }

    fun writeExcel(context: Context, task: Task){
        viewModelScope.launch(Dispatchers.IO) {
            val novels = db.sfNovelsDao().getTaskNovels(task.base.id!!)
            writeToExcelAndShare(task, context, novels)
        }
    }
}

class RecordViewModelFactory(private val db: NovelsDatabase) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(RecordViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return RecordViewModel(db) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}