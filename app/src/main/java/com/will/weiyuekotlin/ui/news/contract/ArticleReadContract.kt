package com.will.weiyuekotlin.ui.news.contract


import com.will.weiyuekotlin.bean.NewsArticleBean
import com.will.weiyuekotlin.ui.base.BaseContract

/**
 * desc: .
 * author: Will .
 * date: 2017/12/8 .
 */
interface ArticleReadContract {

    interface View : BaseContract.BaseView {

        /**
         * 加载新闻内容
         *
         * @param articleBean 新闻内容
         */
        fun loadData(articleBean: NewsArticleBean?)
    }

    interface Presenter : BaseContract.BasePresenter {

        /**
         * 获取新闻内容
         *
         * @param aid 新闻id
         */
        fun getData(aid: String)

    }

}
