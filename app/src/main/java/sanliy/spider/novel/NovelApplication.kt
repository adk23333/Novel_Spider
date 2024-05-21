package sanliy.spider.novel

import android.app.Application
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import sanliy.spider.novel.room.NovelsDatabase

class NovelApplication : Application() {
    private val applicationScope = CoroutineScope(SupervisorJob())
    val database by lazy { NovelsDatabase.getDatabase(this.applicationContext, applicationScope) }
}

