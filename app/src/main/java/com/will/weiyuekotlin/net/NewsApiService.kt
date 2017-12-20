package com.will.weiyuekotlin.net

import com.will.weiyuekotlin.bean.*
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query
import retrofit2.http.Url


/**
 * desc:
 * author: Will .
 * date: 2017/9/2 .
 */
interface NewsApiService {


    //    @GET("ClientNews")
    //    Observable<List<NewsDetail>> getNewsDetail(@Query("id") String id,
    //                                               @Query("action") String action,
    //                                               @Query("pullNum") String pullNum,
    //                                               @Query("uid") String uid,
    //                                               @Query("devid") String devid,
    //                                               @Query("proid") String proid,
    //                                               @Query("vt") String vt,
    //                                               @Query("publishid") String publishid,
    //                                               @Query("screen") String screen,
    //                                               @Query("os") String os,
    //                                               @Query("df") String df,
    //                                               @Query("nw") String nw);

    @GET("ClientNews")
    fun getNewsDetail(@Query("id") id: String,
                      @Query("action") action: String,
                      @Query("pullNum") pullNum: Int
    ): Observable<List<NewsDetail>>

    @GET("api_vampire_article_detail")
    fun getNewsArticleWithSub(@Query("aid") aid: String): Observable<NewsArticleBean>

    @GET
    fun getNewsArticleWithCmpp(@Url url: String,
                               @Query("aid") aid: String): Observable<NewsArticleBean>

    @GET
    fun getNewsImagesWithCmpp(@Url url: String,
                              @Query("aid") aid: String): Observable<NewsImagesBean>

    @GET("NewRelativeVideoList")
    fun getNewsVideoWithCmpp(@Url url: String,
                             @Query("guid") guid: String): Observable<NewsCmppVideoBean>

    @GET("ifengvideoList")
    fun getVideoChannel(@Query("page") page: Int): Observable<List<VideoChannelBean>>

    @GET("ifengvideoList")
    fun getVideoDetail(@Query("page") page: Int,
                       @Query("listtype") listtype: String,
                       @Query("typeid") typeid: String): Observable<List<VideoDetailBean>>


}
