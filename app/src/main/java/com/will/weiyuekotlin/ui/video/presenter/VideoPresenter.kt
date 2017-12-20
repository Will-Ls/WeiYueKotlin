package com.will.weiyuekotlin.ui.video.presenter

import com.will.weiyuekotlin.bean.VideoChannelBean
import com.will.weiyuekotlin.bean.VideoDetailBean
import com.will.weiyuekotlin.net.BaseObserver
import com.will.weiyuekotlin.net.NewsApi
import com.will.weiyuekotlin.ui.base.BasePresenter
import com.will.weiyuekotlin.ui.video.contract.VideoContract
import com.will.weiyuekotlin.utils.applySchedulers
import javax.inject.Inject

/**
 * desc: .
 * author: Will .
 * date: 2017/11/28 .
 */
class VideoPresenter @Inject
constructor(private var mNewsApi: NewsApi) : BasePresenter<VideoContract.View>(), VideoContract.Presenter {

    /**
     * 获取视频频道列表
     *
     */
    override fun getVideoChannel() {
        mNewsApi.videoChannel
                .applySchedulers()
                .compose(mView!!.bindToLife<List<VideoChannelBean>>())
                .subscribe(object : BaseObserver<List<VideoChannelBean>>() {
                    override fun onSucceed(t: List<VideoChannelBean>?) {
                        mView?.loadVideoChannel(t)
                    }

                    override fun onFail(e: Throwable) {
                        mView?.showError()
                    }

                })

    }

    /**
     * 获取视频列表
     *
     * @param page     页码
     * @param listType 默认list
     * @param typeId   频道id
     */
    override fun getVideoDetails(page: Int, listType: String, typeId: String) {
        mNewsApi.getVideoDetail(page, listType, typeId)
                .applySchedulers()
                .compose(mView!!.bindToLife<List<VideoDetailBean>>())
                .subscribe(object : BaseObserver<List<VideoDetailBean>>() {
                    override fun onSucceed(t: List<VideoDetailBean>?) {
                        when {
                            page > 1 -> mView!!.loadMoreVideoDetails(t)
                            else -> mView!!.loadVideoDetails(t)
                        }
                    }

                    override fun onFail(e: Throwable) {
                        mView?.showError()
                    }
                })
    }
}
