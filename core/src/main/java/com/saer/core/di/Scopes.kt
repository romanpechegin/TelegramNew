package com.saer.core.di

import javax.inject.Qualifier
import javax.inject.Scope

@Scope
@Retention(AnnotationRetention.RUNTIME)
annotation class AppScope

@Scope
@Retention(AnnotationRetention.RUNTIME)
annotation class Feature

@Qualifier
@Retention(AnnotationRetention.RUNTIME)
annotation class AuthorisationFlowQualifier
