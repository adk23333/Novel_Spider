package sanliy.spider.novel.model

import java.time.LocalDateTime

interface BaseNovel {
    val novelID: String
    val taskID: Long
    val platform: NovelPlatform
    val novelName: String
    val novelIntro: String
    val authorID: String
    val authorName: String
    val tags: List<Tag>
}

interface SfacgNovel : BaseNovel {
    val lastUpdateTime: LocalDateTime
    val markCount: Int
    val novelCover: String
    val bgBanner: String
    val point: String
    val isFinish: Boolean
    val charCount: Int
    val viewTimes: Int
    val allowDown: Boolean
    val createdTime: LocalDateTime
    val isSensitive: Boolean
    val signStatus: String
    val genre: Genre
    val categoryId: Int //小说类型，如对话小说
}

