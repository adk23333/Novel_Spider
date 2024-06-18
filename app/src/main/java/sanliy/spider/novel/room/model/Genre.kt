package sanliy.spider.novel.room.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import sanliy.spider.novel.model.IGenre
import sanliy.spider.novel.model.NovelPlatform

@Entity(tableName = "genres", primaryKeys = ["genre_id", "platform"])
data class Genre(
    @ColumnInfo(name = "genre_id") override val genreID: String,
    @ColumnInfo(name = "platform") override val platform: NovelPlatform,
    @ColumnInfo(name = "genre_name") override val genreName: String,
) : IGenre {
    companion object {
        val DefaultSFACG = Genre("0", NovelPlatform.SFACG, "全部")

        fun name2save(name: String): String {
            return "genreName:${name}"
        }

        fun id2save(id: String): String {
            return "genreID:${id}"
        }
    }
}