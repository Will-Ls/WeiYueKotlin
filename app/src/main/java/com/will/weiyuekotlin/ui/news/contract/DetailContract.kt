package com.will.weiyuekotlin.ui.news.contract


import com.will.weiyuekotlin.bean.NewsDetail
import com.will.weiyuekotlin.ui.base.BaseContract

/**
 * desc: .
 * author: Will .
 * date: 2017/9/7 .
 */
interface DetailContract {

    interface View : BaseContract.BaseView {

        /**
         * 加载顶部banner数据
         *
         * @param newsDetail
         */
        fun loadBannerData(newsDetail: NewsDetail?)

        /**
         * 加载置顶新闻数据
         *
         * @param newsDetail
         */
        fun loadTopNewsData(newsDetail: NewsDetail?)

        /**
         * 加载新闻数据
         *
         * @param itemBeanList
         */
        fun loadData(itemBeanList: List<NewsDetail.ItemBean>?)

        /**
         * 加载更多新闻数据
         *
         * @param itemBeanList
         */
        fun loadMoreData(itemBeanList: List<NewsDetail.ItemBean>?)

    }

    interface Presenter : BaseContract.BasePresenter {

        /**
         * 获取新闻详细信息
         *
         * @param id      频道ID值
         * @param action  用户操作方式
         * 1：下拉 down
         * 2：上拉 up
         * 3：默认 default
         * @param pullNum 操作次数 累加
         */
        fun getData(id: String, action: String, pullNum: Int)

    }

}
