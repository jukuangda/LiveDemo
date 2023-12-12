package tv.newtv.demo.livedemo.net

import com.google.gson.Gson
import com.google.gson.TypeAdapter
import com.google.gson.TypeAdapterFactory
import com.google.gson.reflect.TypeToken
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonWriter

class NullAdapterFactory : TypeAdapterFactory {

    private fun Class<*>.isKotlinClass(): Boolean {
        return this.declaredAnnotations.any {
            it.annotationClass.qualifiedName == "kotlin.Metadata"
        }
    }

    override fun <T : Any> create(gson: Gson, type: TypeToken<T>): TypeAdapter<T>? {
        val adapter = gson.getDelegateAdapter(this, type)
        val kClass = type.rawType as Class<T>
        return object : TypeAdapter<T>() {
            override fun write(out: JsonWriter?, value: T?) {
                adapter.write(out, value)
            }

            override fun read(`in`: JsonReader?): T? {
                return adapter.read(`in`)?.apply {
                    kClass.declaredFields.forEach { prop ->
                        prop.isAccessible = true
                        if (prop.get(this) == null) {
                            val default = when (prop.type) {
                                String::class.java -> ""
                                Integer::class.java -> 0
                                Long::class.java -> 0L
                                Float::class.java -> 0F
                                Double::class.java -> 0
                                Boolean::class.java -> false
                                else -> null
                            }
                            prop.set(this, default)
                        }
                    }
                }
            }
        }
    }
}