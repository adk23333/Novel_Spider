package sanliy.spider.novel.net.sfacg

import kotlinx.serialization.Serializable
import sanliy.spider.novel.model.NovelPlatform
import sanliy.spider.novel.room.model.TagImpl


@Serializable
data class Status(
    override val httpCode: Int,
    override val errorCode: Int,
    override val msgType: Int,
    override val msg: String?,
) : IStatus

@Serializable
data class ResponseNovels(
    override val status: Status,
    override val data: List<Novels>,
) : IResponseNovels

@Serializable
data class Novels(
    override val novelId: Int,
    override val authorId: Int,
    override val lastUpdateTime: String,
    override val markCount: Int,
    override val novelCover: String,
    override val bgBanner: String,
    override val novelName: String,
    override val point: String,
    override val isFinish: Boolean,
    override val authorName: String,
    override val charCount: Int,
    override val viewTimes: Int,
    override val typeId: Int,
    override val allowDown: Boolean,
    override val addTime: String,
    override val isSensitive: Boolean,
    override val signStatus: String,
    override val categoryId: Int,
    override val expand: Expand,
) : INovels

@Serializable
data class Expand(
    override val typeName: String,
    override val discount: Double,
    override val discountExpireDate: String,
    override val sysTags: List<BaseSysTag>,
    override val intro: String = "",
) : IExpand

@Serializable
data class BaseSysTag(
    override val sysTagId: Int,
    override val tagName: String,
) : IBaseSysTag

fun List<BaseSysTag>.toTags(): List<TagImpl> {
    val temp = mutableListOf<TagImpl>()
    this.forEach {
        temp.add(TagImpl(it.sysTagId.toString(), NovelPlatform.SFACG, it.tagName))
    }
    return temp
}

@Serializable
data class ResponseSysTag(
    override val status: Status,
    override val data: List<SysTag>,
) : IResponseSysTag

@Serializable
data class SysTag(
    override val sysTagId: Int,
    override val tagName: String,
    override var refferTimes: Int,
    override var imageUrl: String,
    override var novelCount: Int,
    override var linkUrl: String?,
    override var introUrl: String?,
    override var isDefault: Boolean,
) : ISysTag


@Serializable
data class NovelType(
    override val typeId: Int,
    override val typeName: String,
    override val expand: String?,
) : INovelType

@Serializable
data class ResponseNovelTypes(
    override val status: Status,
    override val data: List<NovelType>,
) : IResponseNovelType