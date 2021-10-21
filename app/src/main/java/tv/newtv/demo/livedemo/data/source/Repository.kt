package tv.newtv.demo.livedemo.data.source

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import tv.newtv.demo.livedemo.LiveDemoApp
import tv.newtv.demo.livedemo.data.bean.ChannelBean
import tv.newtv.demo.livedemo.util.ExcelUtil
import tv.newtv.demo.livedemo.util.LiveDemoException
import tv.newtv.ottsdk.NewtvSdk

class Repository : DataSource {

    override suspend fun initNewTvSdk(): Flow<Int> {
        return flow {
            NewtvSdk.getInstance().setDebugLevel(3)
            emit(
                NewtvSdk.getInstance().init(
                    LiveDemoApp.appContext, "acdfed1385b0f9e7e55575100aec0314",
                    "50000238", "6a3ae630c0cc3687670ea85b11983270", ""
                )
            )
        }
    }

    override fun getChannels(): Flow<List<ChannelBean>> {
        return flow {
            emit(ExcelUtil.getChannelsByAssets())
        }
    }
}