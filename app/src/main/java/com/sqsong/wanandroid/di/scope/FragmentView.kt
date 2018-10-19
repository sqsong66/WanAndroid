package com.sqsong.wanandroid.di.scope

import javax.inject.Qualifier

@Qualifier
@MustBeDocumented
@Retention(AnnotationRetention.RUNTIME)
annotation class FragmentView(val value: String = "")