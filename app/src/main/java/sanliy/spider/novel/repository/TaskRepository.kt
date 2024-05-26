package sanliy.spider.novel.repository

import kotlinx.coroutines.flow.Flow
import sanliy.spider.novel.model.Task
import sanliy.spider.novel.net.sfacg.model.SysTag
import sanliy.spider.novel.room.TaskDao
import javax.inject.Inject

class TaskRepository @Inject constructor(private val taskDao: TaskDao) {

    fun insertTaskBase(base: Task.Base): Long {
        return taskDao.insertTaskBase(base)
    }

    fun insertSysTag(sysTag: List<SysTag>) {
        taskDao.insertSysTag(sysTag)
    }

    fun updateTask(base: Task.Base) {
        taskDao.updateTask(base)
    }


    fun deleteTasks() {
        taskDao.deleteTasks()
    }


    fun deleteSysTags(taskId: Long) {
        taskDao.deleteSysTags(taskId)
    }

    suspend fun getDeleteTasksId(): List<Long> {
        return taskDao.getDeleteTasksId()
    }


    fun getTasks(): Flow<List<Task>> {
        return taskDao.getTasks()
    }

    fun getMarkTasks(): Flow<List<Task>> {
        return taskDao.getMarkTasks()
    }
}