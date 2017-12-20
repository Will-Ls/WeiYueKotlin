package com.will.weiyuekotlin.ui.video.contract


import com.will.weiyuekotlin.bean.VideoChannelBean
import com.will.weiyuekotlin.bean.VideoDetailBean
import com.will.weiyuekotlin.ui.base.BaseContract

/**
 * desc: .
 * author: Will .
 * date: 2017/9/10 .
 */
interface VideoContract {

    interface View : BaseContract.BaseView {

        /**
         * 加载视频频道列表
         *
         * @param channelBean 频道列表
         */
        fun loadVideoChannel(channelBean: List<VideoChannelBean>?)

        /**
         * 加载频道视频列表
         *
         * @param detailBean 视频列表
         */
        fun loadVideoDetails(detailBean: List<VideoDetailBean>?)

        /**
         * 加载更多频道视频列表
         *
         * @param detailBean 视频列表
         */
        fun loadMoreVideoDetails(detailBean: List<VideoDetailBean>?)

    }

    interface Presenter : BaseContract.BasePresenter {

        /**
         * 获取视频频道列表
         */
        fun getVideoChannel()

        /**
         * 获取视频列表
         *
         * @param page     页码
         * @param listType 默认list
         * @param typeId   频道id
         */
        fun getVideoDetails(page: Int, listType: String, typeId: String)
    }
}
