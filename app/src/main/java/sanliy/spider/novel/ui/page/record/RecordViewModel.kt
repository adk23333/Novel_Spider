package sanliy.spider.novel.ui.page.record

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import sanliy.spider.novel.model.Task
import sanliy.spider.novel.repository.NovelRepository
import sanliy.spider.novel.repository.TaskRepository
import sanliy.spider.novel.share.writeToExcelAndShare
import javax.inject.Inject

@HiltViewModel
class RecordViewModel @Inject constructor(
    private val novelRepository: NovelRepository,
    private val taskRepository: TaskRepository,
) : ViewModel() {

    val tasks by lazy { taskRepository.getTasks() }
    val markTasks by lazy { taskRepository.getMarkTasks() }

    fun delete(task: Task) {
        viewModelScope.launch(Dispatchers.IO) {
            taskRepository.updateTask(task.base.copy(isDelete = true))
            novelRepository.deleteWithTask(task)

            val ids = taskRepository.getDeleteTasksId()
            ids.forEach {
                taskRepository.deleteSysTags(it)
            }
            taskRepository.deleteTasks()

        }
    }

    fun switchIsMark(task: Task) {
        viewModelScope.launch(Dispatchers.IO) {
            taskRepository.updateTask(task.base.copy(isMark = !task.base.isMark))
        }
    }

    fun writeExcel(context: Context, task: Task) {
        viewModelScope.launch(Dispatchers.IO) {
            val novels = novelRepository.getWithTask(task)
            writeToExcelAndShare(task, context, novels)
        }
    }
}
