package com.will.weiyuekotlin.bean

/**
 * desc: .
 * author: Will .
 * date: 2017/9/19 .
 */
class VideoChannelBean {


    /**
     * types : [{"id":9,"name":"娱乐","chType":"video","position":"down"},{"id":18,"name":"美食","chType":"video","position":"down"},{"id":22,"name":"锵锵三人行","chType":"video","position":"down"},{"id":5,"name":"军事","chType":"video","position":"up"},{"id":24,"name":"综艺","chType":"video","position":"up"},{"id":10,"name":"体育","chType":"video","position":"up"},{"id":8,"name":"生活","chType":"video","position":"up"},{"id":25,"name":"纪录片","chType":"video","position":"up"},{"id":4,"name":"社会","chType":"video","position":"up"},{"id":27,"name":"萌萌哒","chType":"video","position":"up"},{"id":2,"name":"段子","chType":"video","position":"down"},{"id":3,"name":"历史","chType":"video","position":"up"},{"id":1,"name":"美女","chType":"video","position":"down"},{"id":23,"name":"鲁豫有约 ","chType":"video","position":"down"}]
     * totalPage : 20
     * currentPage : 1
     * type : list
     * item : [{"documentId":"video_01bd4842-0410-493f-8f46-500c60663578","title":"美日等4国媒体妄称中国间谍船靠近 中方少将怒了","image":"http://d.ifengimg.com/w640_h360_q80/p2.ifengimg.com/a/2017_30/5187db499a933ad.jpg","thumbnail":"http://d.ifengimg.com/w132_h94_q80/p2.ifengimg.com/a/2017_30/5187db499a933ad.jpg","guid":"01bd4842-0410-493f-8f46-500c60663578","type":"phvideo","duration":115,"shareUrl":"http://share.iclient.ifeng.com/sharenews.f?guid=01bd4842-0410-493f-8f46-500c60663578","commentsUrl":"http://share.iclient.ifeng.com/sharenews.f?guid=01bd4842-0410-493f-8f46-500c60663578","video_url":"http://ips.ifeng.com/video19.ifeng.com/video09/2017/07/25/4710314-102-008-0939.mp4","video_size":7731,"praise":"33","tread":"30","playTime":"55296","commentsall":34}]
     */

    var totalPage: Int = 0
    var currentPage: String? = null
    var type: String? = null
    var types: List<TypesBean>? = null
    var item: List<ItemBean>? = null

    class TypesBean {
        /**
         * id : 9
         * name : 娱乐
         * chType : video
         * position : down
         */

        var id: Int = 0
        var name: String? = null
        var chType: String? = null
        var position: String? = null
    }

    class ItemBean {
        /**
         * documentId : video_01bd4842-0410-493f-8f46-500c60663578
         * title : 美日等4国媒体妄称中国间谍船靠近 中方少将怒了
         * image : http://d.ifengimg.com/w640_h360_q80/p2.ifengimg.com/a/2017_30/5187db499a933ad.jpg
         * thumbnail : http://d.ifengimg.com/w132_h94_q80/p2.ifengimg.com/a/2017_30/5187db499a933ad.jpg
         * guid : 01bd4842-0410-493f-8f46-500c60663578
         * type : phvideo
         * duration : 115
         * shareUrl : http://share.iclient.ifeng.com/sharenews.f?guid=01bd4842-0410-493f-8f46-500c60663578
         * commentsUrl : http://share.iclient.ifeng.com/sharenews.f?guid=01bd4842-0410-493f-8f46-500c60663578
         * video_url : http://ips.ifeng.com/video19.ifeng.com/video09/2017/07/25/4710314-102-008-0939.mp4
         * video_size : 7731
         * praise : 33
         * tread : 30
         * playTime : 55296
         * commentsall : 34
         */

        var documentId: String? = null
        var title: String? = null
        var image: String? = null
        var thumbnail: String? = null
        var guid: String? = null
        var type: String? = null
        var duration: Int = 0
        var shareUrl: String? = null
        var commentsUrl: String? = null
        var video_url: String? = null
        var video_size: Int = 0
        var praise: String? = null
        var tread: String? = null
        var playTime: String? = null
        var commentsall: Int = 0
    }
}
