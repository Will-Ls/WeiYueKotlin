package com.will.weiyuekotlin.net

import com.will.weiyuekotlin.bean.FreshNewsArticleBean
import com.will.weiyuekotlin.bean.FreshNewsBean
import com.will.weiyuekotlin.bean.JdDetailBean

import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.http.Url


/**
 * desc: .
 * author: Will .
 * date: 2017/9/27 .
 */
interface JanDanApiService {

    @GET
    fun getFreshNews(@Url url: String, @Query("oxwlxojflwblxbsapi") oxwlxojflwblxbsapi: String,
                     @Query("include") include: String,
                     @Query("page") page: Int,
                     @Query("custom_fields") custom_fields: String,
                     @Query("dev") dev: String
    ): Observable<FreshNewsBean>


    @GET
    fun getDetailData(@Url url: String, @Query("oxwlxojflwblxbsapi") oxwlxojflwblxbsapi: String,
                      @Query("page") page: Int
    ): Observable<JdDetailBean>

    @GET
    fun getFreshNewsArticle(@Url url: String, @Query("oxwlxojflwblxbsapi") oxwlxojflwblxbsapi: String,
                            @Query("include") include: String,
                            @Query("id") id: Int
    ): Observable<FreshNewsArticleBean>

}
