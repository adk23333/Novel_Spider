package sanliy.spider.novel.model

import sanliy.spider.novel.net.sfacg.FinishedStatus
import sanliy.spider.novel.net.sfacg.FreeStatus
import sanliy.spider.novel.net.sfacg.Sort
import sanliy.spider.novel.net.sfacg.UpdatedDate
import java.time.LocalDateTime

interface BaseTask {
    val taskID: Long?
    val taskName: String
    val platform: NovelPlatform
    val isMark: Boolean
    val isDelete: Boolean
    val created: LocalDateTime
    val updated: LocalDateTime
}


interface NovelListTask : BaseTask {
    val novelsNum: Int
    val startPage: Int
    val endPage: Int
}

interface SfacgNovelListTask : NovelListTask {
    val beginCount: Int
    val endCount: Int
    val updateDate: UpdatedDate
    val genre: Genre
    val expand: String
    val isFinish: FinishedStatus
    val isFree: FreeStatus
    val sort: Sort
    val tags: List<Tag>
    val antiTags: List<Tag>
}

