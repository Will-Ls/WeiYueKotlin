package com.will.weiyuekotlin.ui.adapter

import android.app.Activity
import android.graphics.drawable.Drawable
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import com.bumptech.glide.request.target.DrawableImageViewTarget
import com.bumptech.glide.request.transition.Transition
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.will.weiyuekotlin.MyApp
import com.will.weiyuekotlin.R
import com.will.weiyuekotlin.bean.JdDetailBean
import com.will.weiyuekotlin.ui.jandan.ImageBrowseActivity
import com.will.weiyuekotlin.utils.ImageLoaderUtil
import com.will.weiyuekotlin.utils.getTimestampString
import com.will.weiyuekotlin.utils.string2Date
import com.will.weiyuekotlin.widget.ContextUtils
import com.will.weiyuekotlin.widget.MultiImageView
import com.will.weiyuekotlin.widget.ShowMaxImageView

/**
 * desc: .
 * author: Will .
 * date: 2017/9/27 .
 */
class BoredPicAdapter(private val context: Activity, data: List<JdDetailBean.CommentsBean>?) : BaseMultiItemQuickAdapter<JdDetailBean.CommentsBean, BaseViewHolder>(data) {

    init {
        addItemType(JdDetailBean.CommentsBean.TYPE_MULTIPLE, R.layout.item_jandan_pic)
        addItemType(JdDetailBean.CommentsBean.TYPE_SINGLE, R.layout.item_jandan_pic_single)
    }

    override fun convert(viewHolder: BaseViewHolder, commentsBean: JdDetailBean.CommentsBean) {
        viewHolder.setText(R.id.tv_author, commentsBean.comment_author)

        if (!TextUtils.isEmpty(commentsBean.comment_agent)) {
            if (commentsBean.comment_agent!!.contains("Android")) {
                viewHolder.setText(R.id.tv_from, "来自 Android 客户端")
                viewHolder.setVisible(R.id.tv_from, true)
            } else {
                viewHolder.setVisible(R.id.tv_from, false)
            }
        } else {
            viewHolder.setVisible(R.id.tv_from, false)
        }

        viewHolder.setText(R.id.tv_time, getTimestampString(string2Date(commentsBean.comment_date!!, "yyyy-MM-dd HH:mm:ss")))

        if (TextUtils.isEmpty(commentsBean.text_content)) {
            viewHolder.setVisible(R.id.tv_content, false)
        } else {
            viewHolder.setVisible(R.id.tv_content, true)
            val content = commentsBean.text_content!!.replace(" ", "").replace("\r", "").replace("\n", "")
            viewHolder.setText(R.id.tv_content, content)
        }

        viewHolder.setVisible(R.id.img_gif, commentsBean.pics!![0].contains("gif"))
        viewHolder.setVisible(R.id.progress, commentsBean.pics!![0].contains("gif"))
        viewHolder.setText(R.id.tv_like, commentsBean.vote_negative)
        viewHolder.setText(R.id.tv_unlike, commentsBean.vote_positive)
        viewHolder.setText(R.id.tv_comment_count, commentsBean.sub_comment_count)
        viewHolder.addOnClickListener(R.id.img_share)

        when (viewHolder.itemViewType) {
            JdDetailBean.CommentsBean.TYPE_MULTIPLE -> {
                val multiImageView = viewHolder.getView<MultiImageView>(R.id.img)
                viewHolder.setVisible(R.id.img_gif, false)
                multiImageView.setList(commentsBean.pics)
                multiImageView.setOnItemClickListener { _, position ->

                    var imgUrls: List<String> = emptyList()
                    for (i in 0 until commentsBean.pics!!.size) {
                        imgUrls += commentsBean.pics!![i]
                    }
                    ImageBrowseActivity.launch(context, imgUrls, position)
                }
                // viewHolder.getView<View>(R.id.img_share).setOnClickListener { ShareUtils.shareSingleImage(context, commentsBean.pics!![0]) }
            }
            JdDetailBean.CommentsBean.TYPE_SINGLE -> {
                val imageView = viewHolder.getView<ShowMaxImageView>(R.id.img)
                imageView.layoutParams.height = ContextUtils.dip2px(MyApp.instance, 250f)

                imageView.setOnClickListener({
                    var imgUrls: List<String> = emptyList()
                    imgUrls += commentsBean.pics!![0]
                    ImageBrowseActivity.launch(context, imgUrls, 0)
                })
                ImageLoaderUtil.LoadImage(context, commentsBean.pics!![0],
                        object : DrawableImageViewTarget(viewHolder.getView<View>(R.id.img) as ImageView) {
                            override fun onResourceReady(resource: Drawable, transition: Transition<in Drawable>?) {
                                super.onResourceReady(resource, transition)
                                val pmWidth = ContextUtils.getSreenWidth(MyApp.instance)
                                val pmHeight = ContextUtils.getSreenHeight(MyApp.instance)
                                val sal = pmHeight.toFloat() / pmWidth
                                val actualHeight = Math.ceil((sal * resource.intrinsicWidth).toDouble()).toInt()
                                val params = RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, actualHeight)
                                viewHolder.getView<View>(R.id.img).layoutParams = params
                                viewHolder.setVisible(R.id.img_gif, false)
                            }
                        })
                //viewHolder.getView<View>(R.id.img_share).setOnClickListener { ShareUtils.shareText(context, "http://jandan.net/pic/") }
            }
        }

    }
}
