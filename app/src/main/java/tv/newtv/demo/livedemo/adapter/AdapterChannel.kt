package tv.newtv.demo.livedemo.adapter

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.airbnb.lottie.LottieAnimationView
import tv.newtv.demo.livedemo.R
import tv.newtv.demo.livedemo.data.bean.ChannelBean


class AdapterChannel : RecyclerView.Adapter<AdapterChannel.ViewHolder>() {

    private var context: Context? = null

    private var list: List<ChannelBean> = ArrayList()

    private var callback: ((ChannelBean) -> Unit)? = null

    private var selectPos: Int = 0

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        if (context == null) {
            context = parent.context
        }
        return ViewHolder(
            LayoutInflater.from(context).inflate(R.layout.item_channel_list, parent, false)
        )
    }

    override fun getItemCount(): Int = if (list.isNotEmpty()) list.size else 0

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindData(position)
    }

    fun setOnItemClickCallback(callback: (ChannelBean) -> Unit) {
        this.callback = callback
    }

    fun setList(list: List<ChannelBean>) {
        this.list = list
        selectPos = 0
        notifyDataSetChanged()
    }

    fun getSelectPos(): Int {
        return selectPos
    }

    fun playNextChannel() {
        if (selectPos < list.size - 1) {
            selectPos++
            callback?.invoke(list[selectPos])
        }
    }

    fun playPreChannel() {
        if (selectPos > 0) {
            selectPos--
            callback?.invoke(list[selectPos])
        }
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val textProgram: TextView = itemView.findViewById(R.id.textTitle)

        private val viewAnim: LottieAnimationView? = itemView.findViewById(R.id.viewAnim)

        init {
            itemView.setOnClickListener {
                if (selectPos != layoutPosition) {
                    itemView.isSelected = true
                    selectPos = layoutPosition
                    notifyDataSetChanged()
                    callback?.let { it(list[layoutPosition]) }
                }
            }

            itemView.setOnFocusChangeListener { v, hasFocus ->
                if (hasFocus) {
                    itemView.isSelected = true
                } else {
                    if (selectPos != layoutPosition) {
                        itemView.isSelected = false
                    }
                }
            }
        }

        internal fun bindData(position: Int) {
            textProgram.text = list[position].name
            if (position == selectPos) {
                textProgram.setTextColor(context!!.resources.getColor(R.color.color_407BFD))
                viewAnim?.playAnimation()
                viewAnim?.visibility = View.VISIBLE
                selectPos = position
                itemView.isSelected = true
            } else {
                textProgram.setTextColor(Color.WHITE)
                viewAnim?.cancelAnimation()
                viewAnim?.visibility = View.GONE
                itemView.isSelected = false
            }
        }
    }
}