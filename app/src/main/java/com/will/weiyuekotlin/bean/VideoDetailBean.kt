package com.will.weiyuekotlin.bean

/**
 * desc: .
 * author: Will .
 * date: 2017/9/10 .
 */
class VideoDetailBean {

    /**
     * totalPage : 2863
     * currentPage : 1
     * type : list
     * item : [{"documentId":"video_611796","title":"狂甩陈伟霆 刘奕君逆袭成主角？","image":"http://d.ifengimg.com/w640_h360_q80/p0.ifengimg.com/pmop/2017/07/24/40f7fad4-b6a4-4d88-989f-a99eced358e5.jpg","thumbnail":"http://d.ifengimg.com/w132_h94_q80/p0.ifengimg.com/pmop/2017/07/24/40f7fad4-b6a4-4d88-989f-a99eced358e5.jpg","guid":"656e107c-fe02-4c11-8ce8-d7de1111cf8c","type":"phvideo","commentsall":0,"duration":107,"shareUrl":"http://share.iclient.ifeng.com/sharenews.f?guid=656e107c-fe02-4c11-8ce8-d7de1111cf8c","commentsUrl":"http://share.iclient.ifeng.com/sharenews.f?guid=656e107c-fe02-4c11-8ce8-d7de1111cf8c","video_url":"http://ips.ifeng.com/video19.ifeng.com/video09/2017/07/24/5081903-102-9987619-095153.mp4","video_size":7184,"praise":"0","tread":"0","playTime":"6572"}]
     */

    var totalPage: Int = 0
    var currentPage: String? = null
    var type: String? = null
    var item: List<ItemBean>? = null

    class ItemBean {
        /**
         * documentId : video_611796
         * title : 狂甩陈伟霆 刘奕君逆袭成主角？
         * image : http://d.ifengimg.com/w640_h360_q80/p0.ifengimg.com/pmop/2017/07/24/40f7fad4-b6a4-4d88-989f-a99eced358e5.jpg
         * thumbnail : http://d.ifengimg.com/w132_h94_q80/p0.ifengimg.com/pmop/2017/07/24/40f7fad4-b6a4-4d88-989f-a99eced358e5.jpg
         * guid : 656e107c-fe02-4c11-8ce8-d7de1111cf8c
         * type : phvideo
         * commentsall : 0
         * duration : 107
         * shareUrl : http://share.iclient.ifeng.com/sharenews.f?guid=656e107c-fe02-4c11-8ce8-d7de1111cf8c
         * commentsUrl : http://share.iclient.ifeng.com/sharenews.f?guid=656e107c-fe02-4c11-8ce8-d7de1111cf8c
         * video_url : http://ips.ifeng.com/video19.ifeng.com/video09/2017/07/24/5081903-102-9987619-095153.mp4
         * video_size : 7184
         * praise : 0
         * tread : 0
         * playTime : 6572
         */

        var documentId: String? = null
        var title: String? = null
        var image: String? = null
        var thumbnail: String? = null
        var guid: String? = null
        var type: String? = null
        var commentsall: Int = 0
        var duration: Int = 0
        var shareUrl: String? = null
        var commentsUrl: String? = null
        var video_url: String? = null
        var video_size: Int = 0
        var praise: String? = null
        var tread: String? = null
        var playTime: String? = null
    }
}
