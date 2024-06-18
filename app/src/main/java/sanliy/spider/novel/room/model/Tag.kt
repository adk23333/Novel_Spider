package sanliy.spider.novel.room.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import sanliy.spider.novel.model.ITag
import sanliy.spider.novel.model.NovelPlatform

@Entity(tableName = "tags", primaryKeys = ["tag_id", "platform"])
data class Tag(
    @ColumnInfo(name = "tag_id") override val tagID: String,
    @ColumnInfo(name = "platform") override val platform: NovelPlatform,
    @ColumnInfo(name = "tag_name") override val tagName: String,
) : ITag {
    companion object {
        fun toTagNameList(tags: List<Tag>): String {
            return if (tags.isEmpty()) {
                "未配置"
            } else {
                tags.joinToString(",") { it.tagName }
            }
        }
    }
}