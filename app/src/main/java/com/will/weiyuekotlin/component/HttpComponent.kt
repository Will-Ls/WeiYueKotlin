package com.will.weiyuekotlin.component


import com.will.weiyuekotlin.ui.jandan.JdDetailFragment
import com.will.weiyuekotlin.ui.news.ArticleReadActivity
import com.will.weiyuekotlin.ui.news.ImageBrowseActivity
import com.will.weiyuekotlin.ui.news.NewsFragment
import com.will.weiyuekotlin.ui.video.DetailFragment
import com.will.weiyuekotlin.ui.video.VideoFragment
import dagger.Component

/**
 * desc: .
 * author: Will .
 * date: 2017/9/2 .
 */
@Component(dependencies = [(ApplicationComponent::class)])
interface HttpComponent {

    fun inject(videoFragment: VideoFragment)

    fun inject(detailFragment: DetailFragment)

    fun inject(imageBrowseActivity: ImageBrowseActivity)

    fun inject(detailFragment: com.will.weiyuekotlin.ui.news.DetailFragment)

    fun inject(articleReadActivity: ArticleReadActivity)

    fun inject(newsFragment: NewsFragment)

    fun inject(jdDetailFragment: JdDetailFragment)

}
