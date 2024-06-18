package sanliy.spider.novel.ui.page.record

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import sanliy.spider.novel.repository.NovelRepository
import sanliy.spider.novel.repository.TaskRepository
import sanliy.spider.novel.room.model.SfacgNovelListTask
import sanliy.spider.novel.share.writeToExcelAndShare
import javax.inject.Inject

@HiltViewModel
class RecordViewModel @Inject constructor(
    private val novelRepository: NovelRepository,
    private val taskRepository: TaskRepository,
) : ViewModel() {

    val tasks by lazy { taskRepository.getTasks() }
    val markTasks by lazy { taskRepository.getMarkedTasks() }

    fun delete(task: SfacgNovelListTask) {
        viewModelScope.launch(Dispatchers.IO) {
            taskRepository.updateTask(task.copy(isDelete = true))
            novelRepository.deleteSfacgForTask(task)

            taskRepository.deleteExpiredTasks()

        }
    }

    fun switchIsMark(task: SfacgNovelListTask) {
        viewModelScope.launch(Dispatchers.IO) {
            taskRepository.updateTask(task.copy(isMark = !task.isMark))
        }
    }

    fun writeExcel(task: SfacgNovelListTask) {
        viewModelScope.launch(Dispatchers.IO) {
            val novels = novelRepository.getSfacgWithTask(task)
            writeToExcelAndShare(task, novels)
        }
    }
}
