package tv.newtv.demo.livedemo.widget

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.MotionEvent
import android.widget.FrameLayout
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.view_channel.view.*
import tv.newtv.demo.livedemo.R
import tv.newtv.demo.livedemo.adapter.AdapterChannel
import tv.newtv.demo.livedemo.data.bean.ChannelBean

class ChannelView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(
    context,
    attrs,
    defStyleAttr
) {
    private var dismissListener: ChannelWindow.DismissListener? = null

    private val mAdapter: AdapterChannel = AdapterChannel()

    private var lastKeyDown = 0L

    init {
        LayoutInflater.from(context).inflate(R.layout.view_channel, this, true)
        with(view_channel_list) {
            layoutManager = LinearLayoutManager(context)
            this.adapter = mAdapter
            addItemDecoration(DividerDecoration(resources.getDimensionPixelOffset(R.dimen.height_24px)))
//            addOnScrollListener(object : RecyclerView.OnScrollListener() {
//                override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
//                    super.onScrollStateChanged(recyclerView, newState)
//                    when (newState) {
//                        RecyclerView.SCROLL_STATE_IDLE -> dismissListener?.onDelay()
//                        else -> dismissListener?.onCancel()
//                    }
//                }
//            })
        }
    }

    fun setOnDismissListener(listener: ChannelWindow.DismissListener) {
        dismissListener = listener
    }

    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
        when (ev?.action) {
            MotionEvent.ACTION_DOWN -> {
                dismissListener?.onCancel()
                dismissListener?.onDelay()
                Log.i("channelView", "================ Action down ==============")
            }
            MotionEvent.ACTION_MOVE -> {
                dismissListener?.onCancel()
                dismissListener?.onDelay()
                Log.i("channelView", "================ Action move ==============")
            }
            MotionEvent.ACTION_UP -> {
                dismissListener?.onCancel()
                dismissListener?.onDelay()
                Log.i("channelView", "================ Action up ==============")
            }
        }
        return super.dispatchTouchEvent(ev)
    }

    fun setListData(list: List<ChannelBean>) {
        mAdapter.setList(list)
    }

    fun refreshList() {
        mAdapter.notifyDataSetChanged()
    }

    fun setItemClickCallback(callback: (ChannelBean) -> Unit) {
        mAdapter.setOnItemClickCallback {
            callback(it)
            mAdapter.notifyDataSetChanged()
            dismissListener?.onCancel()
            dismissListener?.onDismiss()
        }
    }

    override fun dispatchKeyEvent(event: KeyEvent?): Boolean {
        val cur = System.currentTimeMillis()
        if (cur - lastKeyDown > 200) {
            lastKeyDown = cur
            if (event?.action == KeyEvent.ACTION_DOWN) {
                dismissListener?.onCancel()
                dismissListener?.onDelay()
            }
            return super.dispatchKeyEvent(event)
        }
        return true
    }
}