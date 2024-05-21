package sanliy.spider.novel.net.sfacg.model

import androidx.room.Embedded
import androidx.room.Ignore
import sanliy.spider.novel.net.sfacg.model.RequestNovels.OptionStatus.BOTH


data class RequestNovels(
    @Embedded var charCount: CharCount,
    var expand: String,
    var isfinish: String,
    var isfree: String,
    var size: Int,
    var sort: String,
    var updatedays: Int,
    var type: Int,
) {
    @Ignore
    constructor() : this(
        CharCount(0, 0),
        fullExpend,
        BOTH,
        BOTH,
        Size,
        Sort.latest,
        UpdateDays.noLimit,
        NovelsType.All,
    )

    companion object {
        const val fullExpend = "typeName,sysTags,discount,discountExpireDate"
        const val Size = 20
    }

    object NovelsType {
        const val All = 0
        const val MoHuan = 21
        const val XuanHuan = 22
        const val GuFeng = 23
        const val KeHuan = 24
        const val XiaoYuan = 25
        const val DuShi = 26
        const val YouXi = 27
        const val XuanYi = 29

        object Zh {
            private const val All = "全部"
            private const val MoHuan = "魔幻"
            private const val XuanHuan = "玄幻"
            private const val GuFeng = "古风"
            private const val KeHuan = "科幻"
            private const val XiaoYuan = "校园"
            private const val DuShi = "都市"
            private const val YouXi = "游戏"
            private const val XuanYi = "悬疑"

            var map = mapOf(
                NovelsType.All to All,
                NovelsType.MoHuan to MoHuan,
                NovelsType.XuanHuan to XuanHuan,
                NovelsType.GuFeng to GuFeng,
                NovelsType.KeHuan to KeHuan,
                NovelsType.XiaoYuan to XiaoYuan,
                NovelsType.DuShi to DuShi,
                NovelsType.YouXi to YouXi,
                NovelsType.XuanYi to XuanYi,
            )
        }
    }

    object Sort {
        const val latest = "latest"
        const val bookmark = "bookmark"
        const val ticket = "ticket"
        const val charcount = "charcount"
        const val viewtimes = "viewtimes"
    }

    object UpdateDays {
        const val noLimit = -1
        const val aWeek = 7
        const val aMonth = 30

        object Zh {
            private const val noLimit = "不限"
            private const val aWeek = "七日内"
            private const val aMonth = "本月内"
            var map = mapOf(
                UpdateDays.noLimit to noLimit,
                UpdateDays.aWeek to aWeek,
                UpdateDays.aMonth to aMonth,
            )
        }
    }

    object OptionStatus {
        const val BOTH = "both"
        const val IS = "is"
        const val NOT = "not"

        object ZhFinish {
            private const val BOTH = "不限"
            private const val IS = "已完结"
            private const val NOT = "连载中"
            var map = mapOf(
                OptionStatus.BOTH to BOTH,
                OptionStatus.IS to IS,
                OptionStatus.NOT to NOT
            )
        }

        object ZhFree {
            private const val BOTH = "不限"
            private const val IS = "免费"
            private const val NOT = "VIP"
            var map = mapOf(
                OptionStatus.BOTH to BOTH,
                OptionStatus.IS to IS,
                OptionStatus.NOT to NOT
            )
        }


    }

    data class CharCount(
        var beginCount: Int,
        var endCount: Int,
    ) {
        companion object {
            var map = mapOf(
                CharCount(0, 0) to "不限",
                CharCount(2000000, 0) to "200万字以上",
                CharCount(1000000, 0) to "100万字以上",
                CharCount(500000, 0) to "50万字以上",
                CharCount(200000, 0) to "20万字以上",
                CharCount(1000000, 2000000) to "100-200万字",
                CharCount(500000, 1000000) to "50-100万字",
                CharCount(200000, 500000) to "20-50万字",
                CharCount(0, 200000) to "20万字以下"
            )
        }

    }


}


