package com.saer.core.di

import javax.inject.Qualifier
import javax.inject.Scope

@Scope
@Retention(AnnotationRetention.RUNTIME)
annotation class AppScope

@Qualifier
@Retention(AnnotationRetention.RUNTIME)
annotation class MainDispatcher

@Qualifier
@Retention(AnnotationRetention.RUNTIME)
annotation class IoDispatcher

@Scope
@Retention(AnnotationRetention.RUNTIME)
annotation class LoginFeature

@Scope
@Retention(AnnotationRetention.RUNTIME)
annotation class ChatsFeature