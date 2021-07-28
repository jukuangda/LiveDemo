package tv.newtv.demo.livedemo.view.main

import tv.newtv.demo.livedemo.base.IPresenter
import tv.newtv.demo.livedemo.data.bean.ChannelBean

interface MainContract {

    interface View {

        // 设置2级列表数据
        fun setList(list: List<ChannelBean>)

        // 设置播放器信息，开始播放
        fun setPlayerInfo(info: ChannelBean)

        // 网络异常弹框
        fun showFailView()
    }

    interface Presenter:IPresenter {
        fun init()
        fun getChannels()
    }
}