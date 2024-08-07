package sanliy.spider.novel.net.sfacg

import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.client.request.url
import io.ktor.client.statement.HttpResponse
import sanliy.spider.novel.model.SfacgNovelListTask
import sanliy.spider.novel.net.NetworkModule
import javax.inject.Inject
import javax.inject.Named

class SfacgService @Inject constructor(
    @Named(NetworkModule.SFACG_CLIENT) private val client: HttpClient,
) {
    suspend fun getSysTags(categoryId: Int): HttpResponse {

        return client.get {
            url("/novels/0/sysTags")
            parameter("categoryId", categoryId)
        }
    }


    suspend fun getNovels(page: Int, task: SfacgNovelListTask): HttpResponse {
        return client.get {
            url("novels/${task.genre.genreID}/sysTags/novels")
            parameter("charcountbegin", task.beginCount)
            parameter("charcountend", task.endCount)
            parameter("expand", task.expand)
            parameter("isfinish", task.isFinish)
            parameter("isfree", task.isFree)
            parameter("size", 20)
            parameter("sort", task.sort)
            parameter("updatedays", task.updateDate)
            parameter("systagids", task.tags.joinToString(",") { it.tagID })
            parameter("notexcludesystagids", task.antiTags.joinToString(",") { it.tagID })
            parameter("page", page)
        }
    }

    suspend fun getNovelTypes(): HttpResponse {
        return client.get {
            url("/noveltypes")
        }
    }
}