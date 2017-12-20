package com.will.weiyuekotlin.bean

import java.io.Serializable

/**
 * desc: .
 * author: Will .
 * date: 2017/9/27 .
 */
class FreshNewsBean : Serializable {

    /**
     * status : ok
     * count : 24
     * count_total : 61393
     * pages : 2559
     * posts : [{"id":89822,"url":"http://i.jandan.net/2017/07/25/mirror-x2.html","title":"半夜背后凉飕飕：镜子 两则","excerpt":"两个关于镜子的小故事。","date":"2017-07-25 23:45:59","tags":[{"id":969,"slug":"%e6%95%85%e4%ba%8b","title":"故事","description":"","post_count":90}],"author":{"id":677,"slug":"tunshu","name":"喵熊汪太狼","first_name":"","last_name":"","nickname":"喵熊汪太狼","url":"","description":""},"comment_count":20,"comment_status":"open","custom_fields":{"thumb_c":["http://img.jandan.net/news/2017/07/e449082796cbc599bbe08a93a05fa6ee.jpg"]}}]
     */

    var status: String? = null
    var count: Int = 0
    var count_total: Int = 0
    var pages: Int = 0
    var posts: MutableList<PostsBean>? = null

    class PostsBean : JdBaseBean(), Serializable {
        /**
         * id : 89822
         * url : http://i.jandan.net/2017/07/25/mirror-x2.html
         * title : 半夜背后凉飕飕：镜子 两则
         * excerpt : 两个关于镜子的小故事。
         * date : 2017-07-25 23:45:59
         * tags : [{"id":969,"slug":"%e6%95%85%e4%ba%8b","title":"故事","description":"","post_count":90}]
         * author : {"id":677,"slug":"tunshu","name":"喵熊汪太狼","first_name":"","last_name":"","nickname":"喵熊汪太狼","url":"","description":""}
         * comment_count : 20
         * comment_status : open
         * custom_fields : {"thumb_c":["http://img.jandan.net/news/2017/07/e449082796cbc599bbe08a93a05fa6ee.jpg"]}
         */

        var id: Int = 0
        var url: String? = null
        var title: String? = null
        var excerpt: String? = null
        var date: String? = null
        var author: AuthorBean? = null
        var comment_count: Int = 0
        var comment_status: String? = null
        var custom_fields: CustomFieldsBean? = null
        var tags: List<TagsBean>? = null

        class AuthorBean : Serializable {
            /**
             * id : 677
             * slug : tunshu
             * name : 喵熊汪太狼
             * first_name :
             * last_name :
             * nickname : 喵熊汪太狼
             * url :
             * description :
             */

            var id: Int = 0
            var slug: String? = null
            var name: String? = null
            var first_name: String? = null
            var last_name: String? = null
            var nickname: String? = null
            var url: String? = null
            var description: String? = null
        }

        class CustomFieldsBean : Serializable {
            var thumb_c: List<String>? = null
        }

        class TagsBean : Serializable {
            /**
             * id : 969
             * slug : %e6%95%85%e4%ba%8b
             * title : 故事
             * description :
             * post_count : 90
             */

            var id: Int = 0
            var slug: String? = null
            var title: String? = null
            var description: String? = null
            var post_count: Int = 0
        }
    }
}
