package sanliy.spider.novel.net.sfacg.model

import kotlinx.serialization.Serializable

@Serializable
data class ResultNovels(
    val status: Status = Status(),
    val data: List<Novels> = listOf(),
) {
    @Serializable
    data class Novels(
        val novelId: Int = 0,
        val authorId: Int = 0,
        val lastUpdateTime: String = "",
        val markCount: Int = 0,
        val novelCover: String = "",
        val bgBanner: String = "",
        val novelName: String = "",
        val point: String = "",
        val isFinish: Boolean = false,
        val authorName: String = "",
        val charCount: Int = 0,
        val viewTimes: Int = 0,
        val typeId: Int = 0,
        val allowDown: Boolean = false,
        val addTime: String = "",
        val isSensitive: Boolean = false,
        val signStatus: String = SignStatus.VIP.value,
        val categoryId: Int = 0,
        val expand: Expand = Expand(),
    )

    @Serializable
    data class Expand(
        val typeName: String = "",
        val discount: Double = 1.0,
        val discountExpireDate: String = "",
        val sysTags: List<SysTag> = listOf(),
    )

    @Serializable
    data class SysTag(
        val sysTagId: Int = 0,
        val tagName: String = "",
    )

    enum class SignStatus(val value: String) {
        VIP("VIP"),
        Primary("普通");
    }
}








