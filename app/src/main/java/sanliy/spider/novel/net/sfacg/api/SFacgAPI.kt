package sanliy.spider.novel.net.sfacg.api

import okhttp3.OkHttpClient
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import retrofit2.http.QueryMap
import sanliy.spider.novel.net.sfacg.model.ResultNovels
import sanliy.spider.novel.net.sfacg.model.ResultSysTag


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
            return Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(SfacgAPI::class.java)

        }
    }
}