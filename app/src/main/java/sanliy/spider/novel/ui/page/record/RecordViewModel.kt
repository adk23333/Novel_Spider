package sanliy.spider.novel.ui.page.record

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import sanliy.spider.novel.repository.FileRepository
import sanliy.spider.novel.repository.NovelRepository
import sanliy.spider.novel.repository.TaskRepository
import sanliy.spider.novel.room.model.SfacgNovelListTaskImpl
import javax.inject.Inject

@HiltViewModel
class RecordViewModel @Inject constructor(
    private val novelRepository: NovelRepository,
    private val taskRepository: TaskRepository,
    private val fileRepository: FileRepository,
) : ViewModel() {

    val tasks by lazy { taskRepository.getTasks() }
    val markTasks by lazy { taskRepository.getMarkedTasks() }

    fun delete(task: SfacgNovelListTaskImpl) {
        viewModelScope.launch(Dispatchers.IO) {
            taskRepository.updateTask(task.copy(isDelete = true))
            novelRepository.deleteSfacgForTask(task)

            taskRepository.deleteExpiredTasks()

        }
    }

    fun switchIsMark(task: SfacgNovelListTaskImpl) {
        viewModelScope.launch(Dispatchers.IO) {
            taskRepository.updateTask(task.copy(isMark = !task.isMark))
        }
    }

    fun writeExcel(task: SfacgNovelListTaskImpl) {
        viewModelScope.launch(Dispatchers.IO) {
            val novels = novelRepository.getSfacgWithTask(task)
            val fileName = "ID-${task.taskID}-${task.taskName}.xlsx"
            fileRepository.saveToExcel(novels, fileName)
            fileRepository.shareFile(fileName)
        }
    }
}
