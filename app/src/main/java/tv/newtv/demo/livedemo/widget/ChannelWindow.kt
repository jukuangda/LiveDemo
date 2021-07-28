package tv.newtv.demo.livedemo.widget

import android.content.Context
import android.graphics.drawable.ColorDrawable
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.PopupWindow
import kotlinx.coroutines.*
import tv.newtv.demo.livedemo.R
import tv.newtv.demo.livedemo.data.bean.ChannelBean

class ChannelWindow(
    val context: Context
) : PopupWindow(
    ChannelView(context),
    ViewGroup.LayoutParams.WRAP_CONTENT,
    ViewGroup.LayoutParams.MATCH_PARENT,
    true
), CoroutineScope by MainScope() {
    private val channelView: ChannelView by lazy { contentView as ChannelView }

    init {
        isTouchable = true
        isFocusable = true
        setBackgroundDrawable(ColorDrawable())
        animationStyle = R.style.anim_pop_left_style
        isOutsideTouchable = true
        channelView.setOnDismissListener(object : DismissListener {

            override fun onDismiss() {
                dismiss()
            }

            override fun onCancel() {
                coroutineContext.cancelChildren()
            }

            override fun onDelay() {
                delayDismiss()
            }
        })
    }

    fun show(parent: View) {
        showAtLocation(parent, Gravity.START, 0, 0)
        delayDismiss()
    }

    fun playNextChannel(){
        channelView.playNextChannel()
    }

    fun playPreChannel(){
        channelView.playPreChannel()
    }

    fun refreshList() {
        channelView.refreshList()
    }

    fun setListData(list: List<ChannelBean>) {
        channelView.setListData(list)
    }

    fun setItemClickCallback(callback: (ChannelBean) -> Unit) {
        channelView.setItemClickCallback(callback)
    }

    override fun dismiss() {
        super.dismiss()
        coroutineContext.cancelChildren()
    }

    interface DismissListener {

        fun onDismiss()

        fun onCancel()

        fun onDelay()
    }

    private fun delayDismiss() {
        launch {
            delay(5000)
            if (isActive) {
                dismiss()
            }
        }
    }
}