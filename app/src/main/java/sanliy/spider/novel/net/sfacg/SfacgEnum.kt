package sanliy.spider.novel.net.sfacg


enum class FinishedStatus(val value: String, val zh: String) {
    BOTH("both", "不限"),
    COMPLETED("is", "已完结"),
    SERIALIZING("not", "连载中");

    companion object {
        fun fromValue(value: String) = entries.first { it.value == value }
    }
}

enum class FreeStatus(val value: String, val zh: String) {
    BOTH("both", "不限"),
    FREE("is", "免费"),
    VIP("not", "VIP");

    companion object {
        fun fromValue(value: String) = entries.first { it.value == value }
    }
}

enum class UpdatedDate(val value: Int, val zh: String) {
    UN_LIMIT(-1, "不限"),
    A_WEEK(7, "七日内"),
    A_MONTH(30, "本月内");

    companion object {
        fun fromValue(value: Int) = entries.first { it.value == value }
    }
}

enum class Sort(val value: String) {
    LATEST("latest"),
    BOOKMARK("bookmark"),
    TICKET("ticket"),
    CHARCOUNT("charcount"),
    VIEWTIMES("viewtimes");
}

enum class CharCount(
    val beginCount: Int,
    val endCount: Int,
    val zh: String,
) {
    UN_LIMIT(0, 0, "不限"),
    SIZE_2M_0(2000000, 0, "200万字以上"),
    SIZE_1M_0(1000000, 0, "100万字以上"),
    SIZE_50K_0(500000, 0, "50万字以上"),
    SIZE_20K_0(200000, 0, "20万字以上"),
    SIZE_1M_2M(1000000, 2000000, "100-200万字"),
    SIZE_50K_1M(500000, 1000000, "50-100万字"),
    SIZE_20K_50K(200000, 500000, "20-50万字"),
    SIZE_0_20K(0, 200000, "20万字以下");

    companion object {
        fun fromValue(begin: Int, end: Int) =
            entries.first { it.beginCount == begin && it.endCount == end }
    }
}

