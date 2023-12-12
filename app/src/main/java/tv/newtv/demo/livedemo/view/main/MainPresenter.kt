package tv.newtv.demo.livedemo.view.main

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import tv.newtv.demo.livedemo.base.IPresenter
import tv.newtv.demo.livedemo.data.bean.ChannelBean
import tv.newtv.demo.livedemo.data.source.DataSource
import tv.newtv.demo.livedemo.util.Constants
import tv.newtv.demo.livedemo.util.exceptionHandlingBoc
import tv.newtv.demo.livedemo.util.next

class MainPresenter(
    private val dataSource: DataSource,
    private val view: MainContract.View
) : IPresenter, MainContract.Presenter, CoroutineScope by MainScope() {

    private var list: List<ChannelBean>? = null

    override fun init() {
        launch {
            view.showProgressDialog(1, null)
            dataSource.initNewTvSdk()
                .next({
                    if (it == 0) {
                        getChannels()
                    }
                })
        }
    }

    override fun getChannels() {
        launch {
            dataSource.getChannels()
                .next({
                    list = it
                    view.setList(it)
                    getCntvLiveVdn(it[0])
                })
        }
    }

    override fun getCntvLiveVdn(channel: ChannelBean) {
        launch {
            view.showProgressDialog(1, null)
            if (channel.type == "3") {
                dataSource.getCntvLiveVdn(Constants.CNTV_VDN_PARAM + channel.url)
                    .next({ vdn ->
                        view.setPlayerInfo(channel.also {
                            vdn.hlsUrl?.hls1?.let { hls ->
                                channel.type = "1"
                                channel.url = hls
                            }
                        })
                    })
            } else {
                view.setPlayerInfo(channel)
            }
        }
    }
}