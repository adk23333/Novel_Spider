package sanliy.spider.novel.net.sfacg.api

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import retrofit2.http.QueryMap
import sanliy.spider.novel.net.sfacg.model.ResultNovels
import sanliy.spider.novel.net.sfacg.model.ResultSysTag


private val json = Json { ignoreUnknownKeys = true }

interface SfacgAPI {

    @GET("novels/0/sysTags")
    suspend fun getSysTags(@Query("categoryId") categoryId: Int): Response<ResultSysTag>

    @GET("novels/{type}/sysTags/novels")
    suspend fun getNovels(
        @Path("type") novelsType: Int,
        @QueryMap map: Map<String, String>,
    ): Response<ResultNovels>

    companion object {
        private val client = OkHttpClient.Builder()
//            .readTimeout(2, TimeUnit.SECONDS)
//            .writeTimeout(2, TimeUnit.SECONDS)
            .addInterceptor(SFacgInterceptor())
            .build()

        private const val BASE_URL = "https://api.sfacg.com/"

        fun createSfacgAPI(): SfacgAPI {
            val contentType = "application/json".toMediaType()
            val json = Json {
                ignoreUnknownKeys = false
                isLenient = true
            }
            return Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(client)
                .addConverterFactory(json.asConverterFactory(contentType))
                .build()
                .create(SfacgAPI::class.java)

        }
    }
}