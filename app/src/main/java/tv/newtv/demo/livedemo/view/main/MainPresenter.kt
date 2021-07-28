package tv.newtv.demo.livedemo.view.main

import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import tv.newtv.demo.livedemo.base.BasePresenter
import tv.newtv.demo.livedemo.data.bean.ChannelBean
import tv.newtv.demo.livedemo.data.source.DataSource

class MainPresenter(
    private val dataSource: DataSource,
    private val view: MainContract.View
) : BasePresenter(), MainContract.Presenter, CoroutineScope by MainScope() {

    private var list: List<ChannelBean>? = null

    override fun init() {
        launch {
            if (dataSource.initNewTvSdk() == 0) {
                getChannels()
            }
        }
    }

    override fun getChannels() {
        dataSource.getChannels()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                list = it
                view.setList(it)
                view.setPlayerInfo(it[0])
            }, {
                it.printStackTrace()
            }).also {
                addDisposable(it)
            }
    }
}