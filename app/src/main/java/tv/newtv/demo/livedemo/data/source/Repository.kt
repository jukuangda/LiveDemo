package tv.newtv.demo.livedemo.data.source

import io.reactivex.Observable
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import tv.newtv.demo.livedemo.LiveDemoApp
import tv.newtv.demo.livedemo.data.bean.ChannelBean
import tv.newtv.demo.livedemo.util.ExcelUtil
import tv.newtv.ottsdk.NewtvSdk

class Repository : DataSource {

    private val ioDispatcher = Dispatchers.IO

    override suspend fun initNewTvSdk(): Int {
        return withContext(ioDispatcher) {
            NewtvSdk.getInstance().setDebugLevel(3)
            // 测试环境
            return@withContext NewtvSdk.getInstance().init(
                LiveDemoApp.appContext, "acdfed1385b0f9e7e55575100aec0314",
                "50000238", "6a3ae630c0cc3687670ea85b11983270", ""
            )
        }
    }

    override fun getChannels(): Observable<List<ChannelBean>> {
        return Observable.just("")
            .map {
                ExcelUtil.getChannelsByAssets()
            }
    }
}