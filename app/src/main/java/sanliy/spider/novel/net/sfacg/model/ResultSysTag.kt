package sanliy.spider.novel.net.sfacg.model

import androidx.room.Entity
import androidx.room.Ignore
import kotlinx.serialization.Serializable

@Serializable
data class ResultSysTag(
    val status: Status = Status(),
    val data: List<SysTag> = listOf(),
)

@Serializable
@Entity(primaryKeys = ["sysTagId", "sysTagTaskId", "notexcludesystagTaskId"])
data class SysTag(
    var sysTagId: Int = 0,
    var sysTagTaskId: Long = 0,
    var notexcludesystagTaskId: Long = 0,
    var refferTimes: Int = 0,
    var tagName: String = "",
    var imageUrl: String = "",
    var novelCount: Int = 0,
    @Ignore var linkUrl: String? = null,
    @Ignore var introUrl: String? = null,
    @Ignore var isDefault: Boolean = false,
)
