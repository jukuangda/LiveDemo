package tv.newtv.demo.livedemo.util

import android.widget.Toast
import com.bumptech.glide.load.HttpException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import tv.newtv.demo.livedemo.LiveDemoApp
import tv.newtv.ottsdk.common.NTException
import java.net.ConnectException
import java.net.SocketException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

//collect异常没法呗上游catch捕获逻辑放在onEach做
suspend fun <T> Flow<T>.next(bloc: suspend (value: T) -> Unit): Unit = flowOn(Dispatchers.IO).onEach(bloc).catch { error ->
    if (error is ConnectException || error is UnknownHostException || error is SocketException) {
        Toast.makeText(LiveDemoApp.appContext, "网络连接异常", Toast.LENGTH_LONG).show()
    } else if (error is HttpException) {
        Toast.makeText(LiveDemoApp.appContext, "网络请求异常", Toast.LENGTH_LONG).show()
    } else if (error is SocketTimeoutException) {
        Toast.makeText(LiveDemoApp.appContext, "网络请求超时", Toast.LENGTH_LONG).show()
    } else if (error is NTException) {
        Toast.makeText(LiveDemoApp.appContext, "网络异常:${error.retCode}", Toast.LENGTH_LONG).show()
    } else {
        Toast.makeText(LiveDemoApp.appContext, "未知异常清稍后重试", Toast.LENGTH_LONG).show()
    }
}.collect()