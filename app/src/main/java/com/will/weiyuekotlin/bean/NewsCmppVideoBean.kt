package com.will.weiyuekotlin.bean

/**
 * desc: .
 * author: Will .
 * date: 2017/9/21 .
 */
class NewsCmppVideoBean {

    var singleVideoInfo: List<SingleVideoInfoBean>? = null

    class SingleVideoInfoBean {
        /**
         * videoURLLow : http://ips.ifeng.com/video19.ifeng.com/video09/2017/04/27/3373150-280-068-072537.mp4
         * videoURLMid : http://ips.ifeng.com/video19.ifeng.com/video09/2017/04/27/3373150-280-068-072537.mp4
         * videoSizeLow : 278
         * videoSizeMid : 278
         * videoURLHigh : http://ips.ifeng.com/video19.ifeng.com/video09/2017/04/27/3373150-102-009-072537.mp4
         * videoSizeHigh : 444
         * audioURL : http://ips.ifeng.com/video19.ifeng.com/video09/2017/04/27/3373150-535-066-072537.mp3
         * GUID : 01dfe20b-35ac-4c25-85e4-7ef16c291595
         * praise : 0
         * tread : 0
         * playTime : 5939
         * imgURL : http://d.ifengimg.com/w200_h150/p2.ifengimg.com/a/2017_29/49853f88e91c6c7.jpg
         * smallImgURL : http://d.ifengimg.com/w120_h90/p2.ifengimg.com/a/2017_29/49853f88e91c6c7.jpg
         * largeImgURL : http://d.ifengimg.com/w480_h360/p2.ifengimg.com/a/2017_29/49853f88e91c6c7.jpg
         * richText :
         * videoPublishTime : 2017-04-14 09:08:29
         * shareURL : http://share.iclient.ifeng.com/sharenews.f?guid=01dfe20b-35ac-4c25-85e4-7ef16c291595
         * commentsUrl : http://share.iclient.ifeng.com/sharenews.f?guid=01dfe20b-35ac-4c25-85e4-7ef16c291595
         * type : phvideo
         * id :
         * statisticID : 81-84
         * title : 健身达人演示腹肌最强训练
         * videoLength : 00:00:07
         * longTitle : 健身达人演示腹肌最强训练
         * columnName : 好身材练出来
         * CP : 今日头条
         * collect :
         * lastPlayedTime :
         * status : 1
         * columnId : 8421
         * weMedia : {"headPic":"http://p2.ifengimg.com/ifengimcp/pic/20160804/5e302c40fd343f6915c5_size32_w200_h200.jpg","name":"好身材练出来","desc":"腹肌，人鱼线，马甲线统统都是这样练出来的","id":"8421","type":"normal"}
         * newStatus : 0
         */

        var videoURLLow: String? = null
        var videoURLMid: String? = null
        var videoSizeLow: Int = 0
        var videoSizeMid: Int = 0
        var videoURLHigh: String? = null
        var videoSizeHigh: Int = 0
        var audioURL: String? = null
        var guid: String? = null
        var praise: String? = null
        var tread: String? = null
        var playTime: String? = null
        var imgURL: String? = null
        var smallImgURL: String? = null
        var largeImgURL: String? = null
        var richText: String? = null
        var videoPublishTime: String? = null
        var shareURL: String? = null
        var commentsUrl: String? = null
        var type: String? = null
        var id: String? = null
        var statisticID: String? = null
        var title: String? = null
        var videoLength: String? = null
        var longTitle: String? = null
        var columnName: String? = null
        var cp: String? = null
        var collect: String? = null
        var lastPlayedTime: String? = null
        var status: Int = 0
        var columnId: String? = null
        var weMedia: WeMediaBean? = null
        var newStatus: String? = null

        class WeMediaBean {
            /**
             * headPic : http://p2.ifengimg.com/ifengimcp/pic/20160804/5e302c40fd343f6915c5_size32_w200_h200.jpg
             * name : 好身材练出来
             * desc : 腹肌，人鱼线，马甲线统统都是这样练出来的
             * id : 8421
             * type : normal
             */

            var headPic: String? = null
            var name: String? = null
            var desc: String? = null
            var id: String? = null
            var type: String? = null
        }
    }
}
