package tv.newtv.demo.livedemo.util

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import retrofit2.CallAdapter
import retrofit2.Retrofit
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type

class FlowCallAdapterFactory private constructor(
    private val isAsync: Boolean,
    private val needRetry: Boolean,
    private val dispatcher: CoroutineDispatcher? = null
) : CallAdapter.Factory(){

    companion object {
        fun create(needRetry: Boolean = true) = FlowCallAdapterFactory(isAsync = false, needRetry = needRetry)
        fun createAsync(needRetry: Boolean = true) = FlowCallAdapterFactory(isAsync = true, needRetry = needRetry)
        fun createWithScheduler(dispatcher: CoroutineDispatcher, needRetry: Boolean = true) =
            FlowCallAdapterFactory(isAsync = false, dispatcher = dispatcher, needRetry = needRetry)
    }

    override fun get(
        returnType: Type,
        annotations: Array<Annotation>,
        retrofit: Retrofit
    ): CallAdapter<*, *>? {
        if (getRawType(returnType) != Flow::class.java) {
            return null
        }

        require(returnType is ParameterizedType) { "Call return type must be parameterized as Flow<Foo> or Flow<? extends Foo>" }
        val observableType = getParameterUpperBound(0, returnType)

        return FlowCallAdapter<Any>(observableType, isAsync, needRetry, dispatcher)
    }


}