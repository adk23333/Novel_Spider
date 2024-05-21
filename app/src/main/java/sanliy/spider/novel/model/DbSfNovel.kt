package sanliy.spider.novel.model

import androidx.room.Entity
import sanliy.spider.novel.net.sfacg.model.ResultNovels

@Entity(tableName = "novels", primaryKeys = ["taskId", "novelId"])
data class DbSfNovel(
    val taskId: Long,
    val novelId: Int,

    val authorId: Int,
    val lastUpdateTime: String,
    val markCount: Int,
    val novelCover: String,
    val bgBanner: String,
    val novelName: String,
    val point: String,
    val isFinish: Boolean,
    val authorName: String,
    val charCount: Int,
    val viewTimes: Int,
    val typeId: Int,
    val allowDown: Boolean,
    val addTime: String,
    val isSensitive: Boolean,
    val signStatus: String,
    val categoryId: Int,

    val typeName: String,

    val tag1: String?,
    val tag2: String?,
    val tag3: String?,
    val tag4: String?,
) {
    constructor(
        taskId: Long,
        novels: ResultNovels.Novels,
    ) : this(
        taskId,
        novels.novelId,
        novels.authorId,
        novels.lastUpdateTime,
        novels.markCount,
        novels.novelCover,
        novels.bgBanner,
        novels.novelName,
        novels.point,
        novels.isFinish,
        novels.authorName,
        novels.charCount,
        novels.viewTimes,
        novels.typeId,
        novels.allowDown,
        novels.addTime,
        novels.isSensitive,
        novels.signStatus,
        novels.categoryId,
        novels.expand.typeName,
        novels.expand.sysTags.getOrNull(0)?.tagName,
        novels.expand.sysTags.getOrNull(1)?.tagName,
        novels.expand.sysTags.getOrNull(2)?.tagName,
        novels.expand.sysTags.getOrNull(3)?.tagName,
    )
}
