package com.will.weiyuekotlin.ui.adapter

import android.graphics.Bitmap
import android.graphics.Color
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.helper.ItemTouchHelper
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.TranslateAnimation
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView

import com.chad.library.adapter.base.BaseMultiItemQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.will.weiyuekotlin.R
import com.will.weiyuekotlin.bean.Channel
import com.will.weiyuekotlin.widget.channelDialog.OnChannelListener

/**
 * desc: .
 * author: Will .
 * date: 2017/12/9 .
 */
class NewAdapter(data: List<Channel>, private val mItemTouchHelper: ItemTouchHelper) : BaseMultiItemQuickAdapter<Channel, BaseViewHolder>(data) {
    private var mIsEdit: Boolean = false
    private var mRecyclerView: RecyclerView? = null
    private var startTime: Long = 0

    private var onChannelListener: OnChannelListener? = null

    private val myChannelSize: Int
        get() {
            return mData.indices
                    .asSequence()
                    .map { mData[it] as Channel }
                    .count { it.itemType == Channel.TYPE_MY_CHANNEL }
        }

    /**
     * 我的频道最后一个的position
     * 找到第一个直接返回
     * @return
     */
    private val myLastPosition: Int
        get() {
            for (i in mData.size - 1 downTo -1 + 1) {
                val channel = mData[i] as Channel
                if (Channel.TYPE_MY_CHANNEL == channel.itemType) {
                    return i
                }
            }
            return -1
        }

    /**
     * 获取推荐频道列表的第一个position
     *之前找到了第一个pos直接返回
     *if (mOtherFirstPosition != 0) return mOtherFirstPosition;
     *找到第一个直接返回
     */
    private
    val otherFirstPosition: Int
        get() {
            for (i in mData.indices) {
                val channel = mData[i] as Channel
                if (Channel.TYPE_OTHER_CHANNEL == channel.itemType) {
                    return i
                }
            }
            return -1
        }

    private val ANIM_TIME = 360

    init {
        mIsEdit = false
        addItemType(Channel.TYPE_MY, R.layout.item_channel_title)
        addItemType(Channel.TYPE_MY_CHANNEL, R.layout.channel_rv_item)
        addItemType(Channel.TYPE_OTHER, R.layout.item_channel_title)
        addItemType(Channel.TYPE_OTHER_CHANNEL, R.layout.channel_rv_item)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        mRecyclerView = parent as RecyclerView
        return super.onCreateViewHolder(parent, viewType)
    }

    fun onChannelListener(onChannelListener: OnChannelListener) {
        this.onChannelListener = onChannelListener
    }

    override fun convert(baseViewHolder: BaseViewHolder, channel: Channel) {
        when (baseViewHolder.itemViewType) {
            Channel.TYPE_MY -> {
                baseViewHolder.setText(R.id.tvTitle, channel.channelName)
                baseViewHolder.getView<View>(R.id.tv_edit).setOnClickListener {
                    if (!mIsEdit) {
                        startEditMode(true)
                    } else {
                        startEditMode(false)
                    }
                }
                baseViewHolder.getView<View>(R.id.tv_sort).tag = true
            }
            Channel.TYPE_MY_CHANNEL -> {
                baseViewHolder.setText(R.id.tv_channelname, channel.channelName)
                        .setVisible(R.id.img_edit, mIsEdit)
                        .addOnClickListener(R.id.img_edit)

                if (channel.channelType != 1) {
                    baseViewHolder.getView<View>(R.id.img_edit).tag = true
                    baseViewHolder.getView<View>(R.id.tv_channelname).tag = false
                } else {
                    baseViewHolder.getView<View>(R.id.tv_channelname).tag = true
                }

                baseViewHolder.getView<View>(R.id.rl_channel).setOnLongClickListener {
                    if (!mIsEdit) {
                        startEditMode(true)
                    }
                    mItemTouchHelper.startDrag(baseViewHolder)
                    false
                }

                baseViewHolder.getView<View>(R.id.rl_channel).setOnTouchListener(View.OnTouchListener { _, event ->
                    if (!mIsEdit) return@OnTouchListener false
                    when (event.action) {
                        MotionEvent.ACTION_DOWN -> startTime = System.currentTimeMillis()
                        MotionEvent.ACTION_MOVE -> if (System.currentTimeMillis() - startTime > SPACE_TIME) {
                            //当MOVE事件与DOWN事件的触发的间隔时间大于100ms时，则认为是拖拽starDrag
                            mItemTouchHelper.startDrag(baseViewHolder)
                        }
                        MotionEvent.ACTION_CANCEL, MotionEvent.ACTION_UP -> startTime = 0
                    }
                    false
                })


                baseViewHolder.getView<View>(R.id.rl_channel).setOnClickListener(View.OnClickListener {
                    //执行删除，移动到推荐频道列表
                    if (mIsEdit) {
                        if (channel.channelType == 1) return@OnClickListener
                        var otherFirstPosition = otherFirstPosition
                        val currentPosition = baseViewHolder.adapterPosition
                        //获取到目标View
                        val targetView = mRecyclerView!!.layoutManager.findViewByPosition(otherFirstPosition)
                        //获取当前需要移动的View
                        val currentView = mRecyclerView!!.layoutManager.findViewByPosition(currentPosition)
                        // 如果targetView不在屏幕内,则indexOfChild为-1  此时不需要添加动画,因为此时notifyItemMoved自带一个向目标移动的动画
                        // 如果在屏幕内,则添加一个位移动画
                        if (mRecyclerView!!.indexOfChild(targetView) >= 0 && otherFirstPosition != -1) {
                            val manager = mRecyclerView!!.layoutManager
                            val spanCount = (manager as GridLayoutManager).spanCount
                            val targetX = targetView.left
                            var targetY = targetView.top
                            val myChannelSize = myChannelSize//这里我是为了偷懒 ，算出来我的频道的大小
                            if (myChannelSize % spanCount == 1) {
                                //我的频道最后一行 之后一个，移动后
                                targetY -= targetView.height
                            }
                            //我的频道 移动到 推荐频道的第一个
                            channel.viewType = Channel.TYPE_OTHER_CHANNEL//改为推荐频道类型
                            channel.isChannelSelect = false

                            onChannelListener?.onMoveToOtherChannel(currentPosition, otherFirstPosition - 1)
                            startAnimation(currentView, targetX, targetY)
                        } else {
                            channel.viewType = Channel.TYPE_OTHER_CHANNEL//改为推荐频道类型
                            channel.isChannelSelect = false
                            if (otherFirstPosition == -1) otherFirstPosition = mData.size
                            onChannelListener?.onMoveToOtherChannel(currentPosition, otherFirstPosition - 1)
                        }
                    } else {
                        onChannelListener?.onFinish(channel.channelName!!)
                    }
                })
            }
            Channel.TYPE_OTHER -> {
                baseViewHolder.setText(R.id.tvTitle, channel.channelName)
                        .setText(R.id.tv_sort, "点击添加频道")
                        .setVisible(R.id.tv_edit, false)
                baseViewHolder.getView<View>(R.id.tv_sort).tag = false
            }
            Channel.TYPE_OTHER_CHANNEL -> {
                baseViewHolder.setText(R.id.tv_channelname, channel.channelName)
                        .setVisible(R.id.img_edit, false)
                baseViewHolder.getView<View>(R.id.tv_channelname).setOnClickListener {
                    var myLastPosition = myLastPosition
                    val currentPosition = baseViewHolder.adapterPosition
                    //获取到目标View
                    val targetView = mRecyclerView!!.layoutManager.findViewByPosition(myLastPosition)
                    //获取当前需要移动的View
                    val currentView = mRecyclerView!!.layoutManager.findViewByPosition(currentPosition)

                    // 如果targetView不在屏幕内,则indexOfChild为-1  此时不需要添加动画,因为此时notifyItemMoved自带一个向目标移动的动画
                    // 如果在屏幕内,则添加一个位移动画
                    if (mRecyclerView!!.indexOfChild(targetView) >= 0 && myLastPosition != -1) {
                        val manager = mRecyclerView!!.layoutManager
                        val spanCount = (manager as GridLayoutManager).spanCount
                        var targetX = targetView.left + targetView.width
                        var targetY = targetView.top

                        val myChannelSize = myChannelSize//这里我是为了偷懒 ，算出来我的频道的大小
                        if (myChannelSize % spanCount == 0) {
                            //添加到我的频道后会换行，所以找到倒数第4个的位置
                            val lastFourthView = mRecyclerView!!.layoutManager.findViewByPosition(myLastPosition - 3)
                            //                                        View lastFourthView = mRecyclerView.getChildAt(getMyLastPosition() - 3);
                            targetX = lastFourthView.left
                            targetY = lastFourthView.top + lastFourthView.height
                        }

                        // 推荐频道 移动到 我的频道的最后一个
                        channel.viewType = Channel.TYPE_MY_CHANNEL//改为推荐频道类型
                        channel.isChannelSelect = true

                        onChannelListener?.onMoveToMyChannel(currentPosition, myLastPosition + 1)
                        startAnimation(currentView, targetX, targetY)
                    } else {
                        channel.viewType = Channel.TYPE_MY_CHANNEL//改为推荐频道类型
                        channel.isChannelSelect = true

                        if (myLastPosition == -1) myLastPosition = 0//我的频道没有了，改成0
                        onChannelListener?.onMoveToMyChannel(currentPosition, myLastPosition + 1)
                    }
                }
            }
        }
    }

    private fun startAnimation(currentView: View, targetX: Int, targetY: Int) {
        val parent = mRecyclerView!!.parent as ViewGroup
        val mirrorView = addMirrorView(parent, currentView)
        val animator = getTranslateAnimator((targetX - currentView.left).toFloat(), (targetY - currentView.top).toFloat())
        currentView.visibility = View.INVISIBLE//暂时隐藏
        mirrorView.startAnimation(animator)
        animator.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationStart(animation: Animation) {}

            override fun onAnimationEnd(animation: Animation) {
                parent.removeView(mirrorView)//删除添加的镜像View
                if (currentView.visibility == View.INVISIBLE) {
                    currentView.visibility = View.VISIBLE//显示隐藏的View
                }
            }

            override fun onAnimationRepeat(animation: Animation) {

            }
        })
    }

    /**
     * 添加需要移动的 镜像View
     */
    private fun addMirrorView(parent: ViewGroup, view: View): ImageView {
        view.destroyDrawingCache()
        //首先开启Cache图片 ，然后调用view.getDrawingCache()就可以获取Cache图片
        view.isDrawingCacheEnabled = true
        val mirrorView = ImageView(view.context)
        //获取该view的Cache图片
        val bitmap = Bitmap.createBitmap(view.drawingCache)
        mirrorView.setImageBitmap(bitmap)
        //销毁掉cache图片
        view.isDrawingCacheEnabled = false
        val locations = IntArray(2)
        view.getLocationOnScreen(locations)//获取当前View的坐标
        val parenLocations = IntArray(2)
        mRecyclerView!!.getLocationOnScreen(parenLocations)//获取RecyclerView所在坐标
        val params = FrameLayout.LayoutParams(bitmap.width, bitmap.height)
        params.setMargins(locations[0], locations[1] - parenLocations[1], 0, 0)
        parent.addView(mirrorView, params)//在RecyclerView的Parent添加我们的镜像View，parent要是FrameLayout这样才可以放到那个坐标点
        return mirrorView
    }

    /**
     * 获取位移动画
     */
    private fun getTranslateAnimator(targetX: Float, targetY: Float): TranslateAnimation {
        val translateAnimation = TranslateAnimation(
                Animation.RELATIVE_TO_SELF, 0f,
                Animation.ABSOLUTE, targetX,
                Animation.RELATIVE_TO_SELF, 0f,
                Animation.ABSOLUTE, targetY)
        // RecyclerView默认移动动画250ms 这里设置360ms 是为了防止在位移动画结束后 remove(view)过早 导致闪烁
        translateAnimation.duration = ANIM_TIME.toLong()
        translateAnimation.fillAfter = true
        return translateAnimation
    }


    private fun startEditMode(isEdit: Boolean) {
        mIsEdit = isEdit
        val visibleChildCount = mRecyclerView!!.childCount
        (0 until visibleChildCount)
                .map { mRecyclerView!!.getChildAt(it) }
                .forEach {
                    it?.let {
                        val imgEdit: ImageView? = it.findViewById(R.id.img_edit)
                        val tvName: TextView? = it.findViewById(R.id.tv_channelname)
                        val tvEdit: TextView? = it.findViewById(R.id.tv_edit)
                        val tvSort: TextView? = it.findViewById(R.id.tv_sort)

                        imgEdit?.let { it.visibility = if (it.tag != null && isEdit) View.VISIBLE else View.INVISIBLE }

                        tvName?.let {
                            it.tag?.let {
                                when {
                                    isEdit && it as Boolean -> tvName.setTextColor(Color.GRAY)
                                    else -> tvName.setTextColor(Color.BLACK)
                                }
                            }
                        }

                        tvEdit?.let {
                            when {
                                isEdit -> it.text = "完成"
                                else -> it.text = "编辑"
                            }
                        }

                        tvSort?.let {
                            when {
                                !(it.tag as Boolean) -> return
                                isEdit -> it.text = "拖动可以排序"
                                else -> it.text = "点击进入频道"
                            }
                        }

                    }
                }
    }

    companion object {
        // touch 间隔时间  用于分辨是否是 "点击"
        private val SPACE_TIME: Long = 100
    }

}
