package sanliy.spider.novel.room.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import sanliy.spider.novel.model.ISfacgNLT
import sanliy.spider.novel.model.NovelPlatform
import sanliy.spider.novel.net.sfacg.CharCount
import sanliy.spider.novel.net.sfacg.FinishedStatus
import sanliy.spider.novel.net.sfacg.FreeStatus
import sanliy.spider.novel.net.sfacg.Sort
import sanliy.spider.novel.net.sfacg.UpdatedDate
import java.time.LocalDateTime

@Entity(tableName = "sfacg_novel_list_task")
data class SfacgNovelListTask(
    @PrimaryKey
    @ColumnInfo("task_id") override var taskID: Long?,
    @ColumnInfo("task_name") override var taskName: String = "DefaultTask",
    @ColumnInfo("platform") override var platform: NovelPlatform = NovelPlatform.SFACG,
    @ColumnInfo("is_mark") override var isMark: Boolean = true,
    @ColumnInfo("is_delete") override var isDelete: Boolean = false,
    @ColumnInfo("created") override var created: LocalDateTime = LocalDateTime.now(),
    @ColumnInfo("updated") override var updated: LocalDateTime = LocalDateTime.now(),
    @ColumnInfo("novel_genre") override var genreID: String = Genre.DefaultSFACG.genreID,
    @ColumnInfo("novels_num") override var novelsNum: Int = 0,
    @ColumnInfo("start_page") override var startPage: Int = 1,
    @ColumnInfo("end_page") override var endPage: Int = 0,
    @ColumnInfo("begin_count") override var beginCount: Int = CharCount.UN_LIMIT.beginCount,
    @ColumnInfo("end_count") override var endCount: Int = CharCount.UN_LIMIT.endCount,
    @ColumnInfo("update_date") override var updateDate: UpdatedDate = UpdatedDate.UN_LIMIT,
    @ColumnInfo("expand") override var expand: String = "typeName,sysTags,discount,discountExpireDate",
    @ColumnInfo("is_finish") override var isFinish: FinishedStatus = FinishedStatus.BOTH,
    @ColumnInfo("is_free") override var isFree: FreeStatus = FreeStatus.BOTH,
    @ColumnInfo("sort") override var sort: Sort = Sort.LATEST,
    @ColumnInfo("novels_id") override var tags: String = "",
    @ColumnInfo("anti_novels_id") override var antiTags: String = "",
) : ISfacgNLT {
    fun addTag(tagID: String): SfacgNovelListTask {
        val tags = this.tags.split(",").toMutableSet()
        val antiTags = this.antiTags.split(",").toMutableSet()

        tags.add(tagID)
        tags.remove("")
        antiTags.remove(tagID)

        return this.copy(tags = tags.joinToString(","), antiTags = antiTags.joinToString(","))
    }

    fun addAntiTag(tagID: String): SfacgNovelListTask {
        val tags = this.tags.split(",").toMutableSet()
        val antiTags = this.antiTags.split(",").toMutableSet()

        antiTags.add(tagID)
        antiTags.remove("")
        tags.remove(tagID)

        return this.copy(tags = tags.joinToString(","), antiTags = antiTags.joinToString(","))
    }

    fun removeTag(tagID: String): SfacgNovelListTask {
        val tags = this.tags.split(",").toMutableSet()
        val antiTags = this.antiTags.split(",").toMutableSet()

        tags.remove(tagID)
        antiTags.remove(tagID)

        return this.copy(tags = tags.joinToString(","), antiTags = antiTags.joinToString(","))
    }

    fun getTagIDs(): List<String> {
        return tags.split(",")
    }

    fun getAntiTagIDs(): List<String> {
        return antiTags.split(",")
    }
}

