package sanliy.spider.novel.model

import java.time.LocalDateTime

interface IBaseNovel {
    val novelID: String
    val taskID: Long
    val platform: NovelPlatform
    val novelName: String
    val novelIntro: String
    val authorID: String
    val authorName: String
    val tags: String
}

interface ISfacgNovel : IBaseNovel {
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
    val genreID: String
    val categoryId: Int //小说类型，如对话小说
}

