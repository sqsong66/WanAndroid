## 概述
本项目是基于数据使用的是鸿洋大神的[玩Android开放API](http://wanandroid.com/blog/show/2)，采用的是Kotlin+MVP+Dagger2+Rxjava架构，界面使用Material Design风格。<br><br>
由于该项目也是自己业余时间用来学习Kotlin和一些新的Android开放框架来写着玩的，可能在架构封装等方面还不是很完善，如果大家有好的意见或者建议，欢迎提[issues](https://github.com/songmao123/WanAndroid/issues)或[Pull requests](https://github.com/songmao123/WanAndroid/pulls)。<br>

## 架构
该项目采用的是Kotlin+MVP+Dagger2+Rxjava架构模式，代码采用[Kotlin](https://kotlinlang.org/)编写，架构参考了[MVPArms](https://github.com/JessYanCoding/MVPArms)框架，并引入了最新版本的[Dagger2](https://github.com/google/dagger)。由于对[Dagger2](https://github.com/google/dagger)的原理还不是太理解，所以项目中部分页面的[Dagger2](https://github.com/google/dagger)使用采取了折中的方案，如果大家有什么好的建议或意见，欢迎大家告诉我一起交流探究。<br>
同时项目中使用了[Retrofit](https://square.github.io/retrofit/)+[Rxjava](https://github.com/ReactiveX/RxJava)的方式来进行HTTP网络请求。<br>
个人非常喜欢Google的Material Design设计风格，项目中引入了Google最新的[Material Components](https://github.com/material-components/material-components-android)，界面遵循Material Design规范。并支持多种主题界面的切换。

## 开源库
### 使用的开源库
1. [RxJava](https://github.com/ReactiveX/RxJava)
2. [Retrofit](https://square.github.io/retrofit/)
3. [Dagger2](https://github.com/google/dagger)
4. [Glide](https://github.com/bumptech/glide)
5. [Auto Size](https://github.com/JessYanCoding/AndroidAutoSize)
6. [ButterKnife](https://github.com/JakeWharton/butterknife)
7. [EventBus](https://github.com/greenrobot/EventBus)

### 参考开源库
1. [MVPArms](https://github.com/JessYanCoding/MVPArms)
2. [PersistentCookieJar](https://github.com/franmontiel/PersistentCookieJar)
3. [MaterialSearchView](https://github.com/MiguelCatalan/MaterialSearchView)
4. ...

在此感谢鸿洋大神提供的[API](http://wanandroid.com/)以及各位开源作者提供的优秀框架及使用。<br>
由于项目还在不断的完善中，也欢迎大家提供更好的参考建议。

## 界面预览
1. Gif预览:<br>
<img src="ScreenShot/gif_preview.gif" width="270" height="480" /><br>

2. 图片预览：<br>
<img src="ScreenShot/image01.png" width="270" height="480" />  <img src="ScreenShot/image02.png" width="270" height="480" />  <img src="ScreenShot/image03.png" width="270" height="480" /><br>
<img src="ScreenShot/image04.png" width="270" height="480" />  <img src="ScreenShot/image05.png" width="270" height="480" />  <img src="ScreenShot/image06.png" width="270" height="480" /><br>
<img src="ScreenShot/image07.png" width="270" height="480" />  <img src="ScreenShot/image08.png" width="270" height="480" />  <img src="ScreenShot/image09.png" width="270" height="480" /><br>
