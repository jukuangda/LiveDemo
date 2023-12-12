package tv.newtv.demo.livedemo.data.source

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import tv.newtv.demo.livedemo.LiveDemoApp
import tv.newtv.demo.livedemo.data.bean.ChannelBean
import tv.newtv.demo.livedemo.data.bean.CntvLiveVdn
import tv.newtv.demo.livedemo.net.HttpClient.service
import tv.newtv.demo.livedemo.util.Constants.CNTV_LIVE_KEY
import tv.newtv.demo.livedemo.util.Constants.CNTV_LIVE_VERSION_CODE
import tv.newtv.demo.livedemo.util.Constants.CNTV_V_TOKEN_POS
import tv.newtv.demo.livedemo.util.Constants.KEY_LIVE_VDN
import tv.newtv.demo.livedemo.util.Constants.uuid
import tv.newtv.demo.livedemo.util.ExcelUtil
import tv.newtv.demo.livedemo.util.md5
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

    override fun getCntvLiveVdn(p2pUrl: String): Flow<CntvLiveVdn> {
        val tsp = System.currentTimeMillis()
        val vc = (tsp.toString() + CNTV_LIVE_VERSION_CODE).md5()
        return service.getCntvLiveVdn(
            KEY_LIVE_VDN,
            p2pUrl,
            tsp.toString(),
            vc,
            uuid,
            null,
            vtokenUrl = null,
            vtokenHeader = null
        )
    }
}