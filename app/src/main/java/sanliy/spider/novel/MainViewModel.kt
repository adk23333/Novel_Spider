package sanliy.spider.novel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import sanliy.spider.novel.model.Task

class MainViewModel : ViewModel() {
    var task by mutableStateOf(Task(null))
}