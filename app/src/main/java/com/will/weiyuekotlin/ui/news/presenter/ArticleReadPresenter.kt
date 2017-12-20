package com.will.weiyuekotlin.ui.news.presenter


import com.will.weiyuekotlin.bean.NewsArticleBean
import com.will.weiyuekotlin.net.BaseObserver
import com.will.weiyuekotlin.net.NewsApi
import com.will.weiyuekotlin.ui.base.BasePresenter
import com.will.weiyuekotlin.ui.news.contract.ArticleReadContract
import com.will.weiyuekotlin.utils.applySchedulers
import javax.inject.Inject


/**
 * desc: .
 * author: Will .
 * date: 2017/12/8 .
 */
class ArticleReadPresenter @Inject
constructor(private var mNewsApi: NewsApi) : BasePresenter<ArticleReadContract.View>(), ArticleReadContract.Presenter {

    /**
     * 获取新闻内容
     *
     * @param aid 新闻id
     */
    override fun getData(aid: String) {
        mNewsApi.getNewsArticle(aid)
                .applySchedulers()
                .compose(mView?.bindToLife<NewsArticleBean>())
                .subscribe(object : BaseObserver<NewsArticleBean>(){
                    override fun onSucceed(t: NewsArticleBean?) {
                        mView?.loadData(t)
                    }

                    override fun onFail(e: Throwable) {
                        mView?.loadData(null)
                    }
                })

    }
}
