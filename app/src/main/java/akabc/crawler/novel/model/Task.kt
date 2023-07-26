package akabc.crawler.novel.model

import akabc.crawler.novel.net.sfacg.model.RequestNovels
import akabc.crawler.novel.net.sfacg.model.SysTag
import akabc.crawler.novel.toIdListString
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.Relation

data class Task(
    @Embedded var base: Base,

    @Relation(parentColumn = "id", entityColumn = "sysTagTaskId")
    var systagids: MutableList<SysTag> = mutableListOf(),
    @Relation(parentColumn = "id", entityColumn = "notexcludesystagTaskId")
    var notexcludesystagids: MutableList<SysTag> = mutableListOf(),
) {
    constructor(id: Long?) : this(Base(id))

    @Entity(tableName = "task")
    data class Base(
        @PrimaryKey(autoGenerate = true) var id: Long?,
        var name: String = "默认任务",
        val app: Int = APP.SFACG.int,
        @Embedded val requestNovels: RequestNovels = RequestNovels(),
        var start: Int = 0,
        var end: Int = 0,
        var isMark: Boolean = true,
        var isDelete: Boolean = false,
    )

    fun toMap(page: Int): MutableMap<String, String> {
        return mutableMapOf(
            "charcountbegin" to base.requestNovels.charCount.beginCount.toString(),
            "charcountend" to base.requestNovels.charCount.endCount.toString(),
            "expand" to base.requestNovels.expand,
            "isfinish" to base.requestNovels.isfinish,
            "isfree" to base.requestNovels.isfree,
            "size" to base.requestNovels.size.toString(),
            "sort" to base.requestNovels.sort,
            "page" to page.toString(),
            "systagids" to systagids.toIdListString(),
            "notexcludesystagids" to notexcludesystagids.toIdListString(),
            "updatedays" to base.requestNovels.updatedays.toString(),
        )
    }
}



object APP {
    object SFACG {
        const val en = "SFACG"
        const val zh = "菠萝包"
        const val int = 0
    }

    fun int2zh(int: Int): String {
        return when (int) {
            0 -> SFACG.zh
            else -> "转换错误"
        }
    }
}