package tv.newtv.demo.livedemo.view.main

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.KeyEvent
import android.view.MotionEvent
import android.view.View
import androidx.lifecycle.LifecycleObserver
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.MainScope
import tv.newtv.demo.livedemo.R
import tv.newtv.demo.livedemo.data.bean.ChannelBean
import tv.newtv.demo.livedemo.data.source.Repository
import tv.newtv.demo.livedemo.widget.ChannelWindow

class MainActivity : AppCompatActivity(), MainContract.View, CoroutineScope by MainScope() {

    private val channelWindow: ChannelWindow by lazy { ChannelWindow(this) }
    private val presenter: MainContract.Presenter by lazy { MainPresenter(Repository(), this) }
    private var mProgressDialogStyle: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        for (observer in lifeCycleObserver()) {
            lifecycle.addObserver(observer)
        }
        initView()
        presenter.init()
    }

    override fun onResume() {
        super.onResume()
        view_player.onStart()
    }

    override fun onPause() {
        super.onPause()
        view_player.onStop()
    }

    private fun initView() {
        channelWindow.setItemClickCallback {
            //切换节目操作
            presenter.getCntvLiveVdn(it)
        }
    }

    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
        when (ev?.action) {
            MotionEvent.ACTION_DOWN -> {
                if (!channelWindow.isShowing) {
                    channelWindow.show(viewRoot)
                }
            }
        }
        return super.dispatchTouchEvent(ev)
    }

    override fun dispatchKeyEvent(event: KeyEvent?): Boolean {
        if (event?.action == KeyEvent.ACTION_UP) {
            if (!channelWindow.isShowing) {
                when (event.keyCode) {
                    KeyEvent.KEYCODE_MEDIA_PREVIOUS,
                    KeyEvent.KEYCODE_DPAD_UP -> {
                        //播放上一个节目
                        channelWindow.playPreChannel()
                        return true
                    }
                    KeyEvent.KEYCODE_MEDIA_NEXT,
                    KeyEvent.KEYCODE_DPAD_DOWN -> {
                        //播放下一个节目
                        channelWindow.playNextChannel()
                        return true
                    }
                }
            }
        }
        if (event?.action == KeyEvent.ACTION_DOWN && (event.keyCode == KeyEvent.KEYCODE_DPAD_CENTER || event.keyCode == KeyEvent.KEYCODE_MENU)) {
            if (!channelWindow.isShowing) {
                channelWindow.show(viewRoot)
            }
        }
        return super.dispatchKeyEvent(event)
    }

    override fun setList(list: List<ChannelBean>) {
        channelWindow.setListData(list)
    }

    override fun setPlayerInfo(info: ChannelBean) {
        view_player.setPlayInfo(info)
    }

    override fun showFailView() {

    }

    override fun showProgressDialog(style: Int, dialogTitle: String?) {
        mProgressDialogStyle = style
        when (style) {
            1 -> view_loading?.showOnlyLoading()
            2 -> view_loading?.showLoadingWithNetSpeed()
            3 -> view_loading?.showSLoadingWithNetSpeedAndTitle(dialogTitle)
        }
    }

    override fun dismissProgressDialog(): Int {
        view_loading?.visibility = View.GONE
        return mProgressDialogStyle
    }

    private fun lifeCycleObserver() = ArrayList<LifecycleObserver>(1).apply { add(presenter) }
}