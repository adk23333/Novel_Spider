package sanliy.spider.novel.room.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import sanliy.spider.novel.model.ISfacgNovel
import sanliy.spider.novel.model.NovelPlatform
import java.time.LocalDateTime

@Entity(tableName = "sfacg_novels")
data class SfacgNovel(
    @PrimaryKey
    @ColumnInfo(name = "novel_id")
    override val novelID: String,
    @ColumnInfo(name = "parent_task_id") override val taskID: Long,
    @ColumnInfo(name = "platform") override val platform: NovelPlatform,
    @ColumnInfo(name = "novel_name") override val novelName: String,
    @ColumnInfo(name = "novel_intro") override val novelIntro: String,
    @ColumnInfo(name = "author_id") override val authorID: String,
    @ColumnInfo(name = "author_name") override val authorName: String,
    @ColumnInfo(name = "tags") override val tags: String,
    @ColumnInfo(name = "last_update_time") override val lastUpdateTime: LocalDateTime,
    @ColumnInfo(name = "mark_count") override val markCount: Int,
    @ColumnInfo(name = "novel_cover") override val novelCover: String,
    @ColumnInfo(name = "bg_banner") override val bgBanner: String,
    @ColumnInfo(name = "point") override val point: String,
    @ColumnInfo(name = "is_finish") override val isFinish: Boolean,
    @ColumnInfo(name = "char_count") override val charCount: Int,
    @ColumnInfo(name = "view_times") override val viewTimes: Int,
    @ColumnInfo(name = "allow_down") override val allowDown: Boolean,
    @ColumnInfo(name = "created_time") override val createdTime: LocalDateTime,
    @ColumnInfo(name = "is_sensitive") override val isSensitive: Boolean,
    @ColumnInfo(name = "sign_status") override val signStatus: String,
    @ColumnInfo(name = "genre") override val genreID: String,
    @ColumnInfo(name = "category_id") override val categoryId: Int,
) : ISfacgNovel