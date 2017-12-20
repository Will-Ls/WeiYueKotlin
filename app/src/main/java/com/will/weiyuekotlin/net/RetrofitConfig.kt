package com.will.weiyuekotlin.net

import android.util.Log
import com.will.weiyuekotlin.MyApp
import com.will.weiyuekotlin.bean.Constants
import com.will.weiyuekotlin.utils.isConnected
import okhttp3.CacheControl
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.RequestBody
import okio.Buffer
import java.io.UnsupportedEncodingException
import java.net.URLDecoder

/**
 * desc:
 * author: Will .
 * date: 2017/9/2 .
 */
object RetrofitConfig {
    private val TAG = "RetrofitConfig"

    //设缓存有效期为1天
    private val CACHE_STALE_SEC = 60 * 60 * 24 * 1
    //查询缓存的Cache-Control设置，为if-only-cache时只查询缓存而不会请求服务器，max-stale可以配合设置缓存失效时间
    private val CACHE_CONTROL_CACHE = "only-if-cached, max-stale=" + CACHE_STALE_SEC
    //查询网络的Cache-Control设置
    //(假如请求了服务器并在a时刻返回响应结果，则在max-age规定的秒数内，浏览器将不会发送对应的请求到服务器，数据由缓存直接返回)
    internal val CACHE_CONTROL_NETWORK = "Cache-Control: public, max-age=3600"
    // 避免出现 HTTP 403 Forbidden，参考：http://stackoverflow.com/questions/13670692/403-forbidden-with-java-but-not-web-browser
    internal val AVOID_HTTP403_FORBIDDEN = "User-Agent: Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.11 (KHTML, like Gecko) Chrome/23.0.1271.95 Safari/537.11"

    /**
     * 云端响应头拦截器，用来配置缓存策略
     * Dangerous interceptor that rewrites the server's cache-control header.
     */
    val sRewriteCacheControlInterceptor: Interceptor = Interceptor { chain ->
        var request = chain.request()
        if (!isConnected(MyApp.instance)) {
            request = request.newBuilder().cacheControl(CacheControl.FORCE_CACHE).build()
            Log.e(TAG, "no network")
        }
        val originalResponse = chain.proceed(request)

        if (isConnected(MyApp.instance)) {
            //有网的时候读接口上的@Headers里的配置，你可以在这里进行统一的设置
            var cacheControl = request.cacheControl().toString()
            originalResponse.newBuilder()
                    .header("Cache-Control", cacheControl)
                    .removeHeader("Pragma")
                    .build()
        } else {
            originalResponse.newBuilder()
                    .header("Cache-Control", "public, " + CACHE_CONTROL_CACHE)
                    .removeHeader("Pragma")
                    .build()
        }
    }

    /**
     * 公共参数
     */
    val sQueryParameterInterceptor: Interceptor = Interceptor { chain ->
        val originalRequest = chain.request()
        val request: Request
        val modifiedUrl = originalRequest.url().newBuilder()
                .addQueryParameter("uid", Constants.uid)
                .addQueryParameter("devid", Constants.uid)
                .addQueryParameter("proid", "ifengnews")
                .addQueryParameter("vt", "5")
                .addQueryParameter("publishid", "6103")
                .addQueryParameter("screen", "1080x1920")
                .addQueryParameter("os", "androidphone")
                .addQueryParameter("df", "android_22")
                .addQueryParameter("nw", "wifi")
                .build()
        request = originalRequest.newBuilder().url(modifiedUrl).build()
        chain.proceed(request)
    }

    /**
     * 打印返回的json数据拦截器
     */
    val sLoggingInterceptor: Interceptor = Interceptor { chain ->
        val request = chain.request()
        val requestBuffer = Buffer()
        if (request.body() != null) {
            request.body().writeTo(requestBuffer)
        } else {
            Log.d("LogTAG", "request.body() == null")
        }
        //打印url信息
        Log.w(TAG, "intercept: " + request.url() + if (request.body() != null) "?" + _parseParams(request.body(), requestBuffer) else "")

        chain.proceed(request)
    }

    @Throws(UnsupportedEncodingException::class)
    private fun _parseParams(body: RequestBody, requestBuffer: Buffer): String {
        return if (body.contentType() != null && !body.contentType().toString().contains("multipart")) {
            URLDecoder.decode(requestBuffer.readUtf8(), "UTF-8")
        } else "null"
    }


}
