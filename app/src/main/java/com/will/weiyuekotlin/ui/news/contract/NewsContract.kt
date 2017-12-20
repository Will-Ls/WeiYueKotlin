package com.will.weiyuekotlin.ui.news.contract


import com.will.weiyuekotlin.bean.Channel
import com.will.weiyuekotlin.ui.base.BaseContract

/**
 * desc: .
 * author: Will .
 * date: 2017/9/7 .
 */
interface NewsContract {

    interface View : BaseContract.BaseView {

        /**
         * 初始化频道
         */
        fun loadData(channels: List<Channel>, otherChannels: List<Channel>)

    }

    interface Presenter : BaseContract.BasePresenter {
        /**
         * 获取频道列表
         */
        fun getChannel()

    }

}
