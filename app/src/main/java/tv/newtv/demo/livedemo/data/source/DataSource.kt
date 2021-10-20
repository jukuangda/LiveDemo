package tv.newtv.demo.livedemo.data.source

import kotlinx.coroutines.flow.Flow
import tv.newtv.demo.livedemo.data.bean.ChannelBean

interface DataSource {
    suspend fun initNewTvSdk(): Flow<Int>
    fun getChannels():Flow<List<ChannelBean>>
}
