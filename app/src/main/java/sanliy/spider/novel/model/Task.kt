package sanliy.spider.novel.model

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.Relation
import sanliy.spider.novel.net.sfacg.model.RequestNovels
import sanliy.spider.novel.net.sfacg.model.SysTag
import sanliy.spider.novel.toIdListString

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
        val app: String = APP.SFACG.name,
        @Embedded val requestNovels: RequestNovels = RequestNovels(),
        var start: Int = 0,
        var end: Int = 0,
        var isMark: Boolean = true,
        var isDelete: Boolean = false,
    )

    fun toMap(page: Int): MutableMap<String, String> {
        return mutableMapOf(
            "charcountbegin" to base.requestNovels.beginCount.toString(),
            "charcountend" to base.requestNovels.endCount.toString(),
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


enum class APP(val zh: String) {
    SFACG("菠萝包");
}