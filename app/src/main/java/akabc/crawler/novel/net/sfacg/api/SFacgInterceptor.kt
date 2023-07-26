package akabc.crawler.novel.net.sfacg.api

import android.util.Log
import okhttp3.Interceptor
import okhttp3.Response

class SFacgInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request().newBuilder()
            .addHeader("Authorization", "Basic YXBpdXNlcjozcyMxLXl0NmUqQWN2QHFlcg==")
            .build()
        Log.d("Network Request", "${request.url}")
        return chain.proceed(request)
    }
}