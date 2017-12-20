package com.will.weiyuekotlin.bean

import com.chad.library.adapter.base.entity.MultiItemEntity

/**
 * desc: .
 * author: Will .
 * date: 2017/9/27 .
 */
class JdDetailBean {

    /**
     * status : ok
     * current_page : 1
     * total_comments : 6292
     * page_count : 252
     * count : 25
     * comments : [{"comment_ID":"3519034","comment_post_ID":"26402","comment_author":"老大哥","comment_author_email":"pqpq3@163.com","comment_author_url":"","comment_author_IP":"218.88.71.77","comment_date":"2017-07-27 10:21:10","comment_date_gmt":"2017-07-27 02:21:10","comment_content":"<img src=\"http://wx4.sinaimg.cn/mw690/006g87eSgy1fhy89a5v21g305k09w7pm.gif\"></img>","comment_karma":"0","comment_approved":"1","comment_agent":"Jandan Android App V4.3.1.1;eyJzaWduIjoiMGFjMzA0YmE0NGY2YWY3ZmY4ZmEwYTUwM2QwYjZiZWEifQ==","comment_type":"","comment_parent":"0","user_id":"0","comment_subscribe":"N","comment_reply_ID":"0","vote_positive":"71","vote_negative":"2","vote_ip_pool":"","sub_comment_count":"6","text_content":"","pics":["http://wx4.sinaimg.cn/mw690/006g87eSgy1fhy89a5v21g305k09w7pm.gif"]}]
     */

    var status: String? = null
    var current_page: Int = 0
    var total_comments: Int = 0
    var page_count: Int = 0
    var count: Int = 0
    var comments: List<CommentsBean>? = null

    class CommentsBean :JdBaseBean(), MultiItemEntity {

        /**
         * comment_ID : 3519034
         * comment_post_ID : 26402
         * comment_author : 老大哥
         * comment_author_email : pqpq3@163.com
         * comment_author_url :
         * comment_author_IP : 218.88.71.77
         * comment_date : 2017-07-27 10:21:10
         * comment_date_gmt : 2017-07-27 02:21:10
         * comment_content : <img src="http://wx4.sinaimg.cn/mw690/006g87eSgy1fhy89a5v21g305k09w7pm.gif"></img>
         * comment_karma : 0
         * comment_approved : 1
         * comment_agent : Jandan Android App V4.3.1.1;eyJzaWduIjoiMGFjMzA0YmE0NGY2YWY3ZmY4ZmEwYTUwM2QwYjZiZWEifQ==
         * comment_type :
         * comment_parent : 0
         * user_id : 0
         * comment_subscribe : N
         * comment_reply_ID : 0
         * vote_positive : 71
         * vote_negative : 2
         * vote_ip_pool :
         * sub_comment_count : 6
         * text_content :
         * pics : ["http://wx4.sinaimg.cn/mw690/006g87eSgy1fhy89a5v21g305k09w7pm.gif"]
         */
        var viewType: Int = 0
        var comment_ID: String? = null
        var comment_post_ID: String? = null
        var comment_author: String? = null
        var comment_author_email: String? = null
        var comment_author_url: String? = null
        var comment_author_IP: String? = null
        var comment_date: String? = null
        var comment_date_gmt: String? = null
        var comment_content: String? = null
        var comment_karma: String? = null
        var comment_approved: String? = null
        var comment_agent: String? = null
        var comment_type: String? = null
        var comment_parent: String? = null
        var user_id: String? = null
        var comment_subscribe: String? = null
        var comment_reply_ID: String? = null
        var vote_positive: String? = null
        var vote_negative: String? = null
        var vote_ip_pool: String? = null
        var sub_comment_count: String? = null
        var text_content: String? = null
        var pics: List<String>? = null

        override fun getItemType(): Int {
            return viewType
        }

        companion object {
            val TYPE_SINGLE = 0
            val TYPE_MULTIPLE = 1
        }
    }
}
