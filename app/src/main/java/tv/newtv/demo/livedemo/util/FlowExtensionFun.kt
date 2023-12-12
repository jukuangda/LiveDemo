package tv.newtv.demo.livedemo.util

import android.widget.Toast
import com.bumptech.glide.load.HttpException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import tv.newtv.demo.livedemo.LiveDemoApp
import tv.newtv.ottsdk.common.NTException
import java.math.BigInteger
import java.net.ConnectException
import java.net.SocketException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import java.security.MessageDigest

//collect异常没法呗上游catch捕获逻辑放在onEach做
suspend fun <T> Flow<T>.next(
    bloc: suspend (value: T) -> Unit,
    errorBlock: (suspend FlowCollector<T>.(cause: Throwable) -> Unit)? = null
): Unit = flowOn(Dispatchers.IO)
    .onEach(bloc)
    .catch(errorBlock ?: exceptionHandlingBoc)
    .collect()

val exceptionHandlingBoc: suspend FlowCollector<Any>.(error: Throwable) -> Unit = { error ->
    if (error is ConnectException || error is UnknownHostException || error is SocketException) {
        Toast.makeText(LiveDemoApp.appContext, "网络连接异常", Toast.LENGTH_LONG).show()
    } else if (error is HttpException) {
        Toast.makeText(LiveDemoApp.appContext, "网络请求异常", Toast.LENGTH_LONG).show()
    } else if (error is SocketTimeoutException) {
        Toast.makeText(LiveDemoApp.appContext, "网络请求超时", Toast.LENGTH_LONG).show()
    } else if (error is NTException) {
        Toast.makeText(LiveDemoApp.appContext, "网络异常:${error.retCode}", Toast.LENGTH_LONG)
            .show()
    } else if (error is LiveDemoException) {
        Toast.makeText(LiveDemoApp.appContext, error.errorMsg, Toast.LENGTH_LONG)
            .show()
    } else {
        Toast.makeText(LiveDemoApp.appContext, "未知异常清稍后重试", Toast.LENGTH_LONG).show()
    }
}

fun String.md5(): String {
    return try {
        // 生成一个MD5加密计算摘要
        val md = MessageDigest.getInstance("MD5")
        // 计算md5函数
        md.update(this.toByteArray())
        // digest()最后确定返回md5 hash值，返回值为8位字符串。因为md5 hash值是16位的hex值，实际上就是8位的字符
        // BigInteger函数则将8位的字符串转换成16位hex值，用字符串来表示；得到字符串形式的hash值
        //一个byte是八位二进制，也就是2位十六进制字符（2的8次方等于16的2次方）
        String.format("%32s", BigInteger(1, md.digest()).toString(16)).replace(' ', '0')
            .toUpperCase()
    } catch (e: Exception) {
        e.printStackTrace()
        ""
    }
}