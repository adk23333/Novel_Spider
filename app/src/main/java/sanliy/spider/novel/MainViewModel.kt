package sanliy.spider.novel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import sanliy.spider.novel.room.model.SfacgNovelListTaskImpl

class MainViewModel : ViewModel() {
    var task by mutableStateOf(SfacgNovelListTaskImpl(null))
}