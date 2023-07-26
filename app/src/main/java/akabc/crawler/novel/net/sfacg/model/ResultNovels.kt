package akabc.crawler.novel.net.sfacg.model

data class ResultNovels(
    val status: Status = Status(),
    val data: List<Novels> = listOf(),
) {
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
        val signStatus: String = SignStatus.VIP,
        val categoryId: Int = 0,
        val expand: Expand = Expand(),
    )


    data class Expand(
        val typeName: String = "",
        val discount: Int = 1,
        val discountExpireDate: String = "",
        val sysTags: List<SysTag> = listOf(),
    )

    data class SysTag(
        val sysTagId: Int = 0,
        val tagName: String = "",
    )

    object SignStatus {
        const val VIP = "VIP"
        const val Primary = "普通"
    }
}








