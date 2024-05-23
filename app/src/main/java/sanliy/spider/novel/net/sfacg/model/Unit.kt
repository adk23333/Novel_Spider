package sanliy.spider.novel.net.sfacg.model

import kotlinx.serialization.Serializable

@Serializable
data class Status(
    val httpCode: Int = 200,
    val errorCode: Int = 200,
    val msgType: Int = 0,
    val msg: String? = null,
)


