package tv.newtv.demo.livedemo.view.main

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import tv.newtv.demo.livedemo.base.IPresenter
import tv.newtv.demo.livedemo.data.bean.ChannelBean
import tv.newtv.demo.livedemo.data.source.DataSource
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
                    view.setPlayerInfo(it[0])
                })
        }
    }
}