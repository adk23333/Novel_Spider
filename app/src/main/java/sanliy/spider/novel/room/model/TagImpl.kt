package sanliy.spider.novel.room.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import kotlinx.serialization.Serializable
import sanliy.spider.novel.model.NovelPlatform
import sanliy.spider.novel.model.Tag

@Entity(tableName = "tags", primaryKeys = ["tag_id", "platform"])
@Serializable
data class TagImpl(
    @ColumnInfo(name = "tag_id") override val tagID: String,
    @ColumnInfo(name = "platform") override val platform: NovelPlatform,
    @ColumnInfo(name = "tag_name") override val tagName: String,
) : Tag

fun List<TagImpl>.toTagNameList(): String {
    return if (this.isEmpty()) {
        "未配置"
    } else {
        this.joinToString(",") { it.tagName }
    }
}