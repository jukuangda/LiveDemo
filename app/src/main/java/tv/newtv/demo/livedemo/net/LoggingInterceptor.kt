package tv.newtv.demo.livedemo.net

import android.text.TextUtils

import com.google.gson.JsonParser
import com.google.gson.JsonSyntaxException

import okhttp3.logging.HttpLoggingInterceptor
import tv.newtv.demo.livedemo.util.Timber

class LoggingInterceptor : HttpLoggingInterceptor.Logger {

    override fun log(message: String) {
        try {
            JsonParser().parse(message)
            if (!TextUtils.isEmpty(message)) {
                Timber.json(message)
            }
        } catch (e: JsonSyntaxException) {
            Timber.d(message)
        }
    }
}
