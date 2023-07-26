package akabc.crawler.novel

import akabc.crawler.novel.room.NovelsDatabase
import android.app.Application
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob

class NovelApplication : Application() {
    private val applicationScope = CoroutineScope(SupervisorJob())
    val database by lazy { NovelsDatabase.getDatabase(this.applicationContext, applicationScope) }
}

