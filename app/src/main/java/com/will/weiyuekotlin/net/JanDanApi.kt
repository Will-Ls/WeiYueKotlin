package com.will.weiyuekotlin.net

import android.support.annotation.StringDef
import com.will.weiyuekotlin.bean.FreshNewsArticleBean
import com.will.weiyuekotlin.bean.FreshNewsBean
import com.will.weiyuekotlin.bean.JdDetailBean
import io.reactivex.Observable
import java.lang.annotation.Retention
import java.lang.annotation.RetentionPolicy

/**
 * desc: .
 * author: Will .
 * date: 2017/9/27 .
 */
 class JanDanApi(private val mService: JanDanApiService) {

    companion object {
        /**
         * 新鲜事
         */
        const val TYPE_FRESH = "get_recent_posts"
        /**
         * 新鲜事文章
         */
        const val TYPE_FRESHARTICLE = "get_post"
        /**
         * 无聊图
         */
        const val TYPE_BORED = "jandan.get_pic_comments"
        /**
         * 妹子图
         */
        const val TYPE_GIRLS = "jandan.get_ooxx_comments"
        /**
         * 段子
         */
        const val TYPE_Duan = "jandan.get_duan_comments"

        private var sInstance: JanDanApi? = null

        fun getInstance(janDanApiService: JanDanApiService): JanDanApi {
            if (sInstance == null)
                sInstance = JanDanApi(janDanApiService)
            return sInstance as JanDanApi
        }
    }

    @StringDef(TYPE_FRESH, TYPE_BORED, TYPE_GIRLS, TYPE_Duan)
    @Retention(RetentionPolicy.SOURCE)
    annotation class Type

    /**
     * 获取新鲜事列表
     *
     * @param page 页码
     * @return
     */
    fun getFreshNews(page: Int): Observable<FreshNewsBean> {
        return mService.getFreshNews(ApiConstants.sJanDanApi, TYPE_FRESH,
                "url,date,tags,author,title,excerpt,comment_count,comment_status,custom_fields",
                page, "thumb_c,views", "1")
    }

    /**
     * 获取 无聊图，妹子图，段子列表
     *
     * @param type [Type]
     * @param page 页码
     * @return
     */
    fun getJdDetails(@Type type: String, page: Int): Observable<JdDetailBean> {
        return mService.getDetailData(ApiConstants.sJanDanApi, type, page)
    }

    /**
     * 获取新鲜事文章详情
     *
     * @param id PostsBean id [FreshNewsBean.PostsBean]
     * @return
     */
    fun getFreshNewsArticle(id: Int): Observable<FreshNewsArticleBean> {
        return mService.getFreshNewsArticle(ApiConstants.sJanDanApi, TYPE_FRESHARTICLE, "content,date,modified", id)
    }

}
