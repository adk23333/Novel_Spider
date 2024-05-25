package sanliy.spider.novel.ui.page.record

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import sanliy.spider.novel.model.Task
import sanliy.spider.novel.room.NovelsDatabase
import sanliy.spider.novel.share.writeToExcelAndShare
import javax.inject.Inject

@HiltViewModel
class RecordViewModel @Inject constructor(val db: NovelsDatabase) : ViewModel() {

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

    fun writeExcel(context: Context, task: Task) {
        viewModelScope.launch(Dispatchers.IO) {
            val novels = db.sfNovelsDao().getTaskNovels(task.base.id!!)
            writeToExcelAndShare(task, context, novels)
        }
    }
}
