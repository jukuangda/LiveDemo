package tv.newtv.demo.livedemo.util

import kotlinx.coroutines.*
import kotlinx.coroutines.channels.*
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.flow.callbackFlow
import retrofit2.Call
import retrofit2.CallAdapter
import retrofit2.Callback
import retrofit2.Response
import java.lang.reflect.Type

@ExperimentalCoroutinesApi
internal class FlowCallAdapter<R>(
    private val responseType: Type,
    private val isAsync: Boolean,
    private val needRetry: Boolean,
    private val dispatcher: CoroutineDispatcher? = null
) : CallAdapter<R, Flow<R>> {

    override fun responseType() = responseType


    override fun adapt(call: Call<R>): Flow<R> {
        var flow = callbackFlow {
            if (isAsync) callEnqueueFlow(call) else callExecuteFlow(call)
            awaitClose {
                call.cancel()
            }
        }

        if (!isAsync && dispatcher != null) {
            flow = flow.flowOn(dispatcher)
        }

        return if (needRetry) flow.retryWhen { _, attempt -> attempt < 2 } else flow
    }
}

@ExperimentalCoroutinesApi
internal fun <R> ProducerScope<R>.callEnqueueFlow(call: Call<R>) {
    call.clone().enqueue(object : Callback<R> {
        override fun onResponse(call: Call<R>, response: Response<R>) {
            processing(response)
        }

        override fun onFailure(call: Call<R>, throwable: Throwable) {
            cancel(CancellationException(throwable.localizedMessage, throwable))
        }
    })
}

@ExperimentalCoroutinesApi
internal fun <R> ProducerScope<R>.callExecuteFlow(call: Call<R>) {
    try {
        processing(call.clone().execute())
    } catch (throwable: Throwable) {
        cancel(CancellationException(throwable.localizedMessage, throwable))
    }
}

@ExperimentalCoroutinesApi
internal fun <R> ProducerScope<R>.processing(response: Response<R>) {
    if (response.isSuccessful) {
        val body = response.body()
        if (body == null) {
            cancel(CancellationException("response body is null"))
        } else {
            trySendBlocking(body)
                .onFailure {
                    cancel(CancellationException(it?.localizedMessage, it))
                }
                .onClosed {
                    cancel(CancellationException(it?.localizedMessage, it))
                }
                .onSuccess {
                    close()
                }
        }
    } else {
        cancel(
            CancellationException(
                response.errorBody()?.string() ?: response.message() ?: "unknown error"
            )
        )
    }

}