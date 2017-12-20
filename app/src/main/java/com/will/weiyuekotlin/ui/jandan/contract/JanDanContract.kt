package com.will.weiyuekotlin.ui.jandan.contract

import com.will.weiyuekotlin.bean.FreshNewsBean
import com.will.weiyuekotlin.bean.JdDetailBean
import com.will.weiyuekotlin.ui.base.BaseContract

/**
 * desc: .
 * author: Will .
 * date: 2017/9/27 .
 */
interface JanDanContract {

    interface View : BaseContract.BaseView {

        /**
         * 加载新鲜事列表
         *
         * @param freshNewsBean 新鲜事列表
         */
        fun loadFreshNews(freshNewsBean: FreshNewsBean?)

        /**
         * 加载更多新鲜事列表
         *
         * @param freshNewsBean 新鲜事列表
         */
        fun loadMoreFreshNews(freshNewsBean: FreshNewsBean?)

        /**
         * 加载 无聊图、妹子图、段子列表
         *
         * @param type  具体看 [com.will.weiyuekotlin.net.JanDanApi.Type]
         * @param jdDetailBean 数据列表
         */
        fun loadDetailData(type: String, jdDetailBean: JdDetailBean?)

        /**
         * 加载更多 无聊图、妹子图、段子列表
         *
         * @param type  具体看 [com.will.weiyuekotlin.net.JanDanApi.Type]
         * @param jdDetailBean 数据列表
         */
        fun loadMoreDetailData(type: String, jdDetailBean: JdDetailBean?)

    }

    interface Presenter : BaseContract.BasePresenter {

        /**
         * 获取新鲜事,无聊图、妹子图、段子列表
         *
         * @param type 具体看 [com.will.weiyuekotlin.net.JanDanApi.Type]
         * @param page 页码
         */
        fun getData(type: String, page: Int)

        /**
         * 获取新鲜事数据
         *
         * @param page 页码
         */
        fun getFreshNews(page: Int)

        /**
         * 获取无聊图、妹子图、段子列表
         *
         * @param type 具体看 [com.will.weiyuekotlin.net.JanDanApi.Type]
         * @param page 页码
         */
        fun getDetailData(type: String, page: Int)

    }
}
