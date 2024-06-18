package sanliy.spider.novel.net

import android.util.Log
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.ktor.client.HttpClient
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.request.header
import io.ktor.serialization.kotlinx.json.json
import javax.inject.Named
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
class NetworkModule {
    @Provides
    @Singleton
    @Named(SFACG_CLIENT)
    fun provideSfacgClient(): HttpClient {
        return HttpClient(OkHttp) {
            expectSuccess = true
            install(ContentNegotiation) {
                json()
            }
            defaultRequest {
                url(SFACG_BASE_URL)
                header("Authorization", "Basic YXBpdXNlcjozcyMxLXl0NmUqQWN2QHFlcg==")
            }
            install(Logging) {
                logger = object : Logger {
                    override fun log(message: String) {
                        Log.d("NetworkModule", "log: $message")
                    }
                }
                level = LogLevel.BODY
            }
        }
    }

    companion object {
        const val SFACG_BASE_URL = "https://api.sfacg.com"
        const val SFACG_CLIENT = "SfacgClient"
    }
}
