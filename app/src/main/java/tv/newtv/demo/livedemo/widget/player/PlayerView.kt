package tv.newtv.demo.livedemo.widget.player

import android.content.Context
import android.os.Handler
import android.util.AttributeSet
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import kotlinx.android.synthetic.main.view_player.view.*
import kotlinx.coroutines.*
import tv.icntv.icntvplayersdk.BasePlayer
import tv.icntv.icntvplayersdk.Constants
import tv.icntv.icntvplayersdk.NewTVPlayerInfo
import tv.icntv.icntvplayersdk.NewTVPlayerInterface
import tv.icntv.icntvplayersdk.wrapper.NewTvPlayerWrapper
import tv.newtv.demo.livedemo.LiveDemoApp
import tv.newtv.demo.livedemo.R
import tv.newtv.demo.livedemo.data.bean.ChannelBean
import tv.newtv.demo.livedemo.view.main.MainActivity

class PlayerView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr), CoroutineScope by MainScope() {
    companion object {
        const val TAG = "LiveDemoPlayer"
    }

    private var playerListener: NewTVPlayerInterface? = null
    private var newTVPlayer: BasePlayer? = null
    private var livePlayInfo: ChannelBean? = null  //直播视频信息
    private var isFirstBufferStart: Boolean = false
    private var jobHideTitle: Job? = null
    private var framePlayer: FrameLayout? = null

    init {
        LayoutInflater.from(context).inflate(R.layout.view_player, this, true)
        initPlayer()
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        Handler().post {
            framePlayer = FrameLayout(context)
            var playerHeight = 0
            var playerWith = 0
            val mayHeight = width / 16 * 9
            if (mayHeight < height) {
                playerWith = width
                playerHeight = mayHeight
            } else {
                playerWith = height / 9 * 16
                playerHeight = height
            }
            addView(framePlayer, LayoutParams(playerWith, playerHeight))
            framePlayer?.let {
                val lp = it.layoutParams as LayoutParams
                lp.gravity = Gravity.CENTER
            }
        }
    }

    override fun onDetachedFromWindow() {
        jobHideTitle?.cancel()
        super.onDetachedFromWindow()
        framePlayer?.let {
            removeView(framePlayer)
        }
        framePlayer = null
    }

    private fun initPlayer() {
        playerListener = object : NewTVPlayerInterface {
            override fun onPrepared(linkedHashMap: LinkedHashMap<String, String>?) {
            }

            override fun onBandWidthUpdate(p0: MutableList<Int>?) {

            }

            override fun onCompletion(i: Int) {
            }

            override fun onBufferStart(s: String?) {
                //仅调用后续调用 activity方法
                showProgressDialog(0, null)
            }

            override fun onBufferEnd(s: String?) {
                dismissProgressDialog()
            }

            override fun onError(i: Int, i1: Int, s: String?) {
                Log.e(TAG, "播放器异常: $s")
//                showFailView()
            }

            override fun onTimeout(i: Int) {
                Log.e(TAG, "播放器超时: $i")
            }
        }
    }

    fun setPlayInfo(liveInfo: ChannelBean) {
        livePlayInfo = liveInfo
        showProgressDialog(-1, null)
        val icntvPlayerInfo = NewTVPlayerInfo()
        icntvPlayerInfo.appKey = "acdfed1385b0f9e7e55575100aec0314"
        icntvPlayerInfo.channeId = "50000238"
        icntvPlayerInfo.appVersion = "test01"
        icntvPlayerInfo.drmUrl = "https://cdrm.live.cntv.cn/udrm/udmCNTVGetLicense"
        icntvPlayerInfo.cdnDispatchURl = "https://cdndispatchnewtv.ottcn.com"
        icntvPlayerInfo.dynamicKeyUrl = "https://k.cloud.ottcn.com"
        icntvPlayerInfo.deviceId = "893e51ccdb4b4af7857a05802678094c"
        icntvPlayerInfo.adModel = 0
        icntvPlayerInfo.extend = ""
        icntvPlayerInfo.duration = -1
        icntvPlayerInfo.playUrl = liveInfo.url
        if (liveInfo.type == "1") {
            icntvPlayerInfo.playType = Constants.PLAY_MODEL_CCTV_DRM_LIVE
        } else {
            icntvPlayerInfo.playType = Constants.PLAY_MODEL_LIVE
        }
        playLive(icntvPlayerInfo)
    }

    private fun playLive(icntvPlayerInfo: NewTVPlayerInfo?) {
        isFirstBufferStart = true
        releasePlayer()
        Log.d(TAG, "set playLive url = " + icntvPlayerInfo!!.playUrl)
        if (!LiveDemoApp.INSTANCE.isBackground) {
            newTVPlayer = NewTvPlayerWrapper.getInstance().getPlayer(
                context, framePlayer!!,
                icntvPlayerInfo, playerListener
            )
        }
    }

    fun showProgressDialog(style: Int, dialogTitle: String?) {
        if (context is MainActivity) {
            (context as MainActivity).let {
                if (isFirstBufferStart || style == -1) {
                    it.showProgressDialog(3, livePlayInfo!!.name)
                } else {
                    it.showProgressDialog(2, null)
                }
                isFirstBufferStart = false
            }
        }
    }

    fun onStart() {
        livePlayInfo?.let {
            setPlayInfo(it)
        }
    }

    fun onStop() {
        releasePlayer()
    }

    fun dismissProgressDialog(): Int {
        if (context is MainActivity) {
            if ((context as MainActivity).dismissProgressDialog() == 3 && newTVPlayer?.isPlaying == true) {
                showViewTitle()
            }
        }
        //外部不应该调用player的dismissProgressDialog所以返回Style-1
        return -1
    }

    private fun releasePlayer() {
        newTVPlayer?.release()
        viewTitle.visibility = GONE
    }

    private fun showViewTitle() {
        viewTitle.text = livePlayInfo?.name
        viewTitle.visibility = VISIBLE
        jobHideTitle = launch {
            delay(5000)
            viewTitle.visibility = GONE
        }
    }
}