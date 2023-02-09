package com.project.kovid.di

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

/**
 * Hilt를 시작하기 위해 @HiltAndroidApp 어노테이션이 달린 Application()을 상속받는 파일
 * AndroidManifest.xml에서 name에 MyApplication을 적용
 * */
@HiltAndroidApp
class MyApplication: Application()