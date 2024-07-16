package sanliy.spider.novel.net.sfacg

import sanliy.spider.novel.room.model.GenreImpl


data class RequestNovels(
    override var beginCount: Int = CharCount.UN_LIMIT.beginCount,
    override var endCount: Int = CharCount.UN_LIMIT.endCount,
    override var expand: String = "typeName,sysTags,discount,discountExpireDate",
    override var isfinish: FinishedStatus = FinishedStatus.BOTH,
    override var isfree: FreeStatus = FreeStatus.BOTH,
    override var size: Int = 20,
    override var sort: Sort = Sort.LATEST,
    override var updatedays: UpdatedDate = UpdatedDate.UN_LIMIT,
    override var type: GenreImpl = GenreImpl.DefaultSFACG,
) : IRequestNovels


