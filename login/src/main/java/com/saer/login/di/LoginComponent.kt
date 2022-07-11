package com.saer.login.di

import androidx.annotation.RestrictTo
import androidx.lifecycle.ViewModel
import com.saer.core.Resources
import com.saer.core.di.Feature
import com.saer.login.ui.EnterPhoneNumberFragment
import dagger.Component
import kotlinx.telegram.core.TelegramFlow
import kotlin.properties.Delegates

@Feature
@Component(dependencies = [LoginDeps::class], modules = [LoginModule::class])
internal interface LoginComponent {

    fun inject(fragment: EnterPhoneNumberFragment)

    @Component.Builder
    interface Builder {

        fun deps(loginDeps: LoginDeps): Builder

        fun build(): LoginComponent
    }
}

interface LoginDeps {
    fun telegramApi(): TelegramFlow
    fun resources(): Resources
}

interface LoginDepsProvider {

    @get:RestrictTo(RestrictTo.Scope.LIBRARY)
    var deps: LoginDeps

    companion object : LoginDepsProvider by LoginDepsStore
}

internal object LoginDepsStore : LoginDepsProvider {

    override var deps: LoginDeps by Delegates.notNull()
}

internal class LoginComponentViewModel: ViewModel() {
    val loginComponent =
        DaggerLoginComponent.builder()
            .deps(LoginDepsProvider.deps)
            .build()
}