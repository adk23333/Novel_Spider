package akabc.crawler.novel

import akabc.crawler.novel.model.Task
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

class MainViewModel: ViewModel() {
    var task by mutableStateOf(Task(null))
}