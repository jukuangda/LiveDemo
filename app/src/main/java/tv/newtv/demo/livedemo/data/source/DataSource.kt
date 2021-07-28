package tv.newtv.demo.livedemo.data.source

import io.reactivex.Observable
import tv.newtv.demo.livedemo.data.bean.ChannelBean
import java.util.*

interface DataSource {
    suspend fun initNewTvSdk(): Int
    fun getChannels():Observable<List<ChannelBean>>
}
