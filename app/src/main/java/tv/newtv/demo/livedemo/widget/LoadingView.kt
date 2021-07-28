package tv.newtv.demo.livedemo.widget

import android.content.Context
import android.graphics.Color
import android.net.TrafficStats
import android.text.TextUtils
import android.util.AttributeSet
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.animation.Animation
import android.view.animation.LinearInterpolator
import android.view.animation.RotateAnimation
import android.widget.FrameLayout
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.view_loading.view.*
import tv.newtv.demo.livedemo.R
import java.util.*
import java.util.concurrent.TimeUnit

class LoadingView : FrameLayout {

    private var mLastTotalRxBytes: Long = 0

    private var mLastTimeStamp: Long = 0

    private var rotateAnimation: RotateAnimation? = null

    private var mAnimating: Boolean = false

    private var isShouldBreak: Boolean = false

    private var mDisposableNetSpeedRefresh: Disposable? = null

    //毫秒转换
    private val speed: String
        get() {
            val nowTotalRxBytes = getTotalRxBytes(context.applicationInfo.uid)
            val nowTimeStamp = System.currentTimeMillis()
            val speed =
                ((nowTotalRxBytes - mLastTotalRxBytes) * 1000 / (nowTimeStamp - mLastTimeStamp)).toInt()
            mLastTimeStamp = nowTimeStamp
            mLastTotalRxBytes = nowTotalRxBytes
            return String.format(Locale.getDefault(), "%d KB/s", speed)
        }

    constructor(context: Context) : super(context) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        init()
    }

    private fun init() {
        LayoutInflater.from(context).inflate(R.layout.view_loading, this, true)

        //设置默认背景色
        this.setBackgroundColor(Color.parseColor("#99000000"))

        //初始化旋转动画
        rotateAnimation = RotateAnimation(
            0f,
            360f,
            Animation.RELATIVE_TO_SELF,
            0.5f,
            Animation.RELATIVE_TO_SELF,
            0.5f
        )
        val li = LinearInterpolator()
        rotateAnimation!!.interpolator = li
        rotateAnimation!!.duration = 2000//设置动画持续周期
        rotateAnimation!!.repeatCount = Animation.INFINITE//设置重复次数
        //rotateAnimation.setFillAfter(true);//动画执行完后是否停留在执行完的状态
        img_loading.animation = rotateAnimation
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        stopAnim()
        mDisposableNetSpeedRefresh?.dispose()
    }

    override fun setVisibility(visibility: Int) {
        super.setVisibility(visibility)
        if (visibility == View.VISIBLE) {
            startAnim()
            if (net_speed.visibility == View.VISIBLE) {
                mDisposableNetSpeedRefresh?.dispose()
                mDisposableNetSpeedRefresh = Observable.interval(0, 1, TimeUnit.SECONDS)
                    .map { l -> speed }
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(
                        { l ->
                            net_speed.text = l
                        },
                        { throwable -> Log.e("获取网速异常", "" + throwable) })
            }
        } else {
            mDisposableNetSpeedRefresh?.dispose()
            stopAnim()
        }
    }

    fun setTitle(title: String?) {
        if (title != null && !TextUtils.isEmpty(title)) {
            loading_title.text = title
        }
    }

    fun setTitleVisibility(visibility: Int) {
        loading_title.visibility = visibility
    }

    fun setNetSpeedVisibility(visibility: Int) {
        net_speed.visibility = visibility
    }

    fun showOnlyLoading() {
        setTitleVisibility(View.GONE)
        setNetSpeedVisibility(View.GONE)
        visibility = View.VISIBLE
    }

    fun showLoadingWithNetSpeed() {
        setTitleVisibility(View.GONE)
        setNetSpeedVisibility(View.VISIBLE)
        visibility = View.VISIBLE
    }

    fun showSLoadingWithNetSpeedAndTitle(title: String?) {
        setTitle(title)
        setTitleVisibility(View.VISIBLE)
        setNetSpeedVisibility(View.VISIBLE)
        visibility = View.VISIBLE
    }

    private fun startAnim() {
        if (rotateAnimation != null && !mAnimating) {
            rotateAnimation!!.start()
            mAnimating = true
        }
    }

    private fun stopAnim() {
        if (rotateAnimation != null && mAnimating) {
            rotateAnimation!!.cancel()
            mAnimating = false
        }
    }

    private fun getTotalRxBytes(uid: Int): Long {
        return if (TrafficStats.getUidRxBytes(uid) == TrafficStats.UNSUPPORTED.toLong())
            0
        else
            TrafficStats
                .getTotalRxBytes() / 1024//转为KB
    }
}
