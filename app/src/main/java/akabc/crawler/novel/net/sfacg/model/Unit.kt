package akabc.crawler.novel.net.sfacg.model

data class Status(
    val httpCode: Int = 200,
    val errorCode: Int = 200,
    val msgType: Int = 0,
    val msg: String? = null,
)


