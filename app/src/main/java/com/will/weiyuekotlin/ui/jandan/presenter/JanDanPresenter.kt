package com.will.weiyuekotlin.ui.jandan.presenter

import com.will.weiyuekotlin.bean.FreshNewsBean
import com.will.weiyuekotlin.bean.JdDetailBean
import com.will.weiyuekotlin.net.BaseObserver
import com.will.weiyuekotlin.net.JanDanApi
import com.will.weiyuekotlin.ui.base.BasePresenter
import com.will.weiyuekotlin.ui.jandan.contract.JanDanContract
import com.will.weiyuekotlin.utils.applySchedulers
import javax.inject.Inject

/**
 * desc: 煎蛋业务类 .
 * author: Will .
 * date: 2017/9/27 .
 */
class JanDanPresenter @Inject
constructor(private var mJanDanApi: JanDanApi) : BasePresenter<JanDanContract.View>(), JanDanContract.Presenter {

    /**
     * 获取新鲜事,无聊图、妹子图、段子列表
     *
     * @param type  [com.will.weiyuekotlin.net.JanDanApi.Type]
     * @param page 获取数据数量
     */
    override fun getData(type: String, page: Int) {
        when (type) {
            JanDanApi.TYPE_FRESH -> getFreshNews(page)
            else -> getDetailData(type, page)
        }
    }

    /**
     * 获取新鲜事列表
     *
     * @param page 获取数据数量
     */
    override fun getFreshNews(page: Int) {
        mJanDanApi.getFreshNews(page)
                .applySchedulers()
                .compose(mView?.bindToLife<FreshNewsBean>())
                .subscribe(object : BaseObserver<FreshNewsBean>() {
                    override fun onSuccess(t: FreshNewsBean?) {
                        when {
                            page > 1 -> mView?.loadMoreFreshNews(t)
                            else -> mView?.loadFreshNews(t)
                        }
                    }

                    override fun onFail(e: Throwable) {
                        when {
                            page > 1 -> mView?.loadMoreFreshNews(null)
                            else -> mView?.loadFreshNews(null)
                        }
                    }
                })

    }

    /**
     * 获取无聊图、妹子图、段子列表
     *
     * @param type  [com.will.weiyuekotlin.net.JanDanApi.Type]
     * @param page 获取数据数量
     */
    override fun getDetailData(type: String, page: Int) {
        mJanDanApi.getJdDetails(type, page)
                .applySchedulers()
                .compose(mView?.bindToLife<JdDetailBean>())
                .map({ jdDetailBean ->
                    jdDetailBean.comments!!
                            .asSequence()
                            .filter { it.pics != null }
                            .forEach {
                                when {
                                    it.pics!!.size > 1 -> it.viewType = JdDetailBean.CommentsBean.TYPE_MULTIPLE
                                    else -> it.viewType = JdDetailBean.CommentsBean.TYPE_SINGLE
                                }
                            }
                    jdDetailBean
                })
                .subscribe(object : BaseObserver<JdDetailBean>() {
                    override fun onSuccess(t: JdDetailBean?) {
                        when {
                            page > 1 -> mView?.loadMoreDetailData(type, t)
                            else -> mView?.loadDetailData(type, t)
                        }
                    }

                    override fun onFail(e: Throwable) {
                        when {
                            page > 1 -> mView?.loadMoreDetailData(type, null)
                            else -> mView?.loadDetailData(type, null)
                        }
                    }
                })
    }
}
