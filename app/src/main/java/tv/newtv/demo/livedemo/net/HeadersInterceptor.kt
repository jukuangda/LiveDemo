package tv.newtv.demo.livedemo.net

import android.text.TextUtils
import okhttp3.HttpUrl
import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException

class HeadersInterceptor : Interceptor {
    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val origUrl = request.url()
        val builder = request.newBuilder()
        val headerUrl = request.header("base-url") ?: ""
        return if (!TextUtils.isEmpty(headerUrl)) {
            val newBaseUrl = HttpUrl.get(headerUrl)
            builder.removeHeader("base-url")
            val newUrl = origUrl.newBuilder()
                .scheme(newBaseUrl.scheme())
                .host(newBaseUrl.host())
                .port(newBaseUrl.port())
                .build()
            chain.proceed(builder.url(newUrl).build())
        } else {
            chain.proceed(request)
        }
    }
}