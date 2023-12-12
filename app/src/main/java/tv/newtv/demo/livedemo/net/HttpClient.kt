package tv.newtv.demo.livedemo.net

import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import tv.newtv.demo.livedemo.util.FlowCallAdapterFactory
import java.util.concurrent.TimeUnit

object HttpClient {

    val service: HttpApiService

    val client: OkHttpClient

    init {
        val logging = HttpLoggingInterceptor(LoggingInterceptor())
        logging.level = HttpLoggingInterceptor.Level.BODY
        client = OkHttpClient.Builder()
            .addInterceptor(logging)
            .addInterceptor(HeadersInterceptor())
            .connectTimeout(10, TimeUnit.SECONDS)
            .readTimeout(10, TimeUnit.SECONDS)
            .writeTimeout(10, TimeUnit.SECONDS)
            .build()
        val retrofit = Retrofit.Builder()
            .baseUrl("https://www.baidu.com")
            .addConverterFactory(ScalarsConverterFactory.create())
            .addConverterFactory(
                GsonConverterFactory.create(
                    GsonBuilder().registerTypeAdapterFactory(NullAdapterFactory())
                        .create()
                )
            )
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addCallAdapterFactory(FlowCallAdapterFactory.create())
            .client(client)
            .build()
        service = retrofit.create(HttpApiService::class.java)
    }
}
