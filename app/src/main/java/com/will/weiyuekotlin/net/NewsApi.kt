package com.will.weiyuekotlin.net

import android.support.annotation.StringDef
import com.will.weiyuekotlin.bean.NewsArticleBean
import com.will.weiyuekotlin.bean.NewsDetail
import com.will.weiyuekotlin.bean.VideoChannelBean
import com.will.weiyuekotlin.bean.VideoDetailBean
import io.reactivex.Observable
import java.lang.annotation.Retention
import java.lang.annotation.RetentionPolicy


/**
 * desc:
 * author: Will .
 * date: 2017/9/2 .
 */
class NewsApi(private val mService: NewsApiService) {

    companion object {
        const val ACTION_DEFAULT = "default"
        const val ACTION_DOWN = "down"
        const val ACTION_UP = "up"

        private var sInstance: NewsApi? = null

        fun getInstance(newsApiService: NewsApiService): NewsApi {
            if (sInstance == null)
                sInstance = NewsApi(newsApiService)
            return sInstance as NewsApi
        }
    }

    @StringDef(ACTION_DEFAULT, ACTION_DOWN, ACTION_UP)
    @Retention(RetentionPolicy.SOURCE)
    annotation class Actions

    /**
     * 获取视频频道列表
     *
     * @return
     */
    val videoChannel: Observable<List<VideoChannelBean>>
        get() = mService.getVideoChannel(1)

    /**
     * 获取新闻详情
     *
     * @param id      频道ID值
     * @param action  用户操作方式
     *                1：下拉 down
     *                2：上拉 up
     *                3：默认 default
     *
     * @param pullNum 操作次数 累加
     * @return
     */
    fun getNewsDetail(id: String, @Actions action: String, pullNum: Int): Observable<NewsDetail>
            = mService.getNewsDetail(id, action, pullNum)
            .flatMap { newsDetails -> Observable.fromIterable(newsDetails) }

    /**
     * 获取新闻文章详情
     *
     * @param aid 文章aid  此处baseurl可能不同，需要特殊处理
     *            aid 以 cmpp 开头则调用 getNewsArticleWithCmpp
     * @return
     */
    fun getNewsArticle(aid: String): Observable<NewsArticleBean> = when {
        aid.startsWith("sub") -> mService.getNewsArticleWithSub(aid)
        else -> mService.getNewsArticleWithCmpp(ApiConstants.sGetNewsArticleCmppApi + ApiConstants.sGetNewsArticleDocCmppApi, aid)
    }

    /**
     * 获取
     *
     * @param page
     * @param listType
     * @param typeId
     * @return
     */
    fun getVideoDetail(page: Int, listType: String, typeId: String): Observable<List<VideoDetailBean>>
            = mService.getVideoDetail(page, listType, typeId)

}
