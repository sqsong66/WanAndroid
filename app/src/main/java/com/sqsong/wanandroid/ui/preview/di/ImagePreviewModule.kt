package com.sqsong.wanandroid.ui.preview.di

import com.sqsong.wanandroid.di.scope.FragmentScope
import com.sqsong.wanandroid.ui.preview.fragment.PreviewFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class ImagePreviewModule {

    @FragmentScope
    @ContributesAndroidInjector
    abstract fun contributePreviewFragment(): PreviewFragment

}