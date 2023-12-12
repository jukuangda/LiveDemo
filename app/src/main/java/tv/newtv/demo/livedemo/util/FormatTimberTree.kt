package tv.newtv.demo.livedemo.util

import android.text.TextUtils
import android.util.Log
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject

class FormatTimberTree(
    var level: Int = Log.VERBOSE,
    var printThread: Boolean = true
) : Timber.DebugTree() {

    private val DIVIDER =
        "------------------------------------------------------------------------------------------"

    init {
        CALL_STACK_INDEX = 6
    }

    override fun isLoggable(tag: String?, priority: Int): Boolean {
        return priority >= level
    }

    override fun wtf(message: String?, vararg args: Any?) {
        super.wtf(getPrintMsg(message), *args)
    }

    override fun wtf(t: Throwable?, message: String?, vararg args: Any?) {
        super.wtf(t, getPrintMsg(message), *args)
    }

    override fun wtf(t: Throwable?) {
        super.wtf(t)
    }

    override fun w(message: String?, vararg args: Any?) {
        super.w(getPrintMsg(message), *args)
    }

    override fun w(t: Throwable?, message: String?, vararg args: Any?) {
        super.w(t, getPrintMsg(message), *args)
    }

    override fun w(t: Throwable?) {
        super.w(t)
    }

    override fun v(message: String?, vararg args: Any?) {
        super.v(getPrintMsg(message), *args)
    }

    override fun v(t: Throwable?, message: String?, vararg args: Any?) {
        super.v(t, getPrintMsg(message), *args)
    }

    override fun v(t: Throwable?) {
        super.v(t)
    }

    override fun log(priority: Int, message: String?, vararg args: Any?) {
        super.log(priority, getPrintMsg(message), *args)
    }

    override fun log(priority: Int, t: Throwable?, message: String?, vararg args: Any?) {
        super.log(priority, t, getPrintMsg(message), *args)
    }

    override fun log(priority: Int, t: Throwable?) {
        super.log(priority, t)
    }

    override fun i(message: String?, vararg args: Any?) {
        super.i(getPrintMsg(message), *args)
    }

    override fun i(t: Throwable?, message: String?, vararg args: Any?) {
        super.i(t, getPrintMsg(message), *args)
    }

    override fun i(t: Throwable?) {
        super.i(t)
    }

    override fun e(message: String?, vararg args: Any?) {
        super.e(getPrintMsg(message), *args)
    }

    override fun e(t: Throwable?, message: String?, vararg args: Any?) {
        super.e(t, getPrintMsg(message), *args)
    }

    override fun e(t: Throwable?) {
        super.e(t)
    }

    override fun d(message: String?, vararg args: Any?) {
        super.d(getPrintMsg(message), *args)
    }

    override fun d(t: Throwable?, message: String?, vararg args: Any?) {
        super.d(t, getPrintMsg(message), *args)
    }

    override fun d(t: Throwable?) {
        super.d(t)
    }

    override fun json(message: String?) {
        super.d(getPrintMsg(formatJson(message)))
    }

    private fun getPrintMsg(msg: String?): String? = if (printThread) """
[${Thread.currentThread().name}]
$DIVIDER
$msg
    """.trimIndent() else msg

    private fun formatJson(json: String?): String {

        if (TextUtils.isEmpty(json)) {
            return "Empty/Null json content"
        }
        try {
            val newJson = json!!.trim { it <= ' ' }
            if (newJson.startsWith("{")) {
                val jsonObject = JSONObject(newJson)
                return jsonObject.toString(2)
            }
            if (newJson.startsWith("[")) {
                val jsonArray = JSONArray(newJson)
                return jsonArray.toString(2)
            }
            return "Invalid Json: $newJson"
        } catch (e: JSONException) {
            return "Invalid Json: $json"
        }
    }
}