package sanliy.spider.novel.net.sfacg

import sanliy.spider.novel.room.model.GenreImpl

interface IStatus {
    val httpCode: Int
    val errorCode: Int
    val msgType: Int
    val msg: String?
}

interface IResponseNovels {
    val status: Status
    val data: List<INovels>
}

interface INovels {
    val novelId: Int
    val authorId: Int
    val lastUpdateTime: String
    val markCount: Int
    val novelCover: String
    val bgBanner: String
    val novelName: String
    val point: String
    val isFinish: Boolean
    val authorName: String
    val charCount: Int
    val viewTimes: Int
    val typeId: Int
    val allowDown: Boolean
    val addTime: String
    val isSensitive: Boolean
    val signStatus: String
    val categoryId: Int
    val expand: IExpand
}

interface IExpand {
    val typeName: String
    val intro: String
    val discount: Double
    val discountExpireDate: String
    val sysTags: List<IBaseSysTag>
}

interface IBaseSysTag {
    val sysTagId: Int
    val tagName: String
}

interface IResponseSysTag {
    val status: IStatus
    val data: List<ISysTag>
}

interface ISysTag : IBaseSysTag {
    var refferTimes: Int
    var imageUrl: String
    var novelCount: Int
    var linkUrl: String?
    var introUrl: String?
    var isDefault: Boolean
}

interface IRequestNovels {
    val beginCount: Int
    val endCount: Int
    val expand: String
    val isfinish: FinishedStatus
    val isfree: FreeStatus
    val size: Int
    val sort: Sort
    val updatedays: UpdatedDate
    val type: GenreImpl
}

interface INovelType {
    val typeId: Int
    val typeName: String
    val expand: String?
}

interface IResponseNovelType {
    val status: IStatus
    val data: List<INovelType>
}

