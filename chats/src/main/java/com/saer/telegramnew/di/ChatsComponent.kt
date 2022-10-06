package com.saer.telegramnew.di

import androidx.annotation.RestrictTo
import androidx.lifecycle.ViewModel
import com.saer.api.TelegramFlow
import com.saer.core.di.ChatsFeature
import com.saer.core.di.IoDispatcher
import com.saer.telegramnew.ui.ListOfChatsViewModel
import dagger.Component
import kotlinx.coroutines.CoroutineDispatcher
import kotlin.properties.Delegates

@ChatsFeature
@Component(
    dependencies = [ChatsDeps::class],
    modules = [ChatsModule::class]
)
internal interface ChatsComponent {

    fun listOfChatsViewModel(): ListOfChatsViewModel.Factory

    @Component.Builder
    interface Builder {

        fun deps(chatsDeps: ChatsDeps): Builder

        fun build(): ChatsComponent
    }


}

interface ChatsDeps {
    fun telegramApi(): TelegramFlow

    @IoDispatcher
    fun ioDispatcher(): CoroutineDispatcher
}

interface ChatsDepsProvider {

    @get:RestrictTo(RestrictTo.Scope.LIBRARY)
    var deps: ChatsDeps

    companion object : ChatsDepsProvider by ChatsDepsStore
}

internal object ChatsDepsStore : ChatsDepsProvider {

    override var deps: ChatsDeps by Delegates.notNull()
}

internal class ChatsComponentViewModel : ViewModel() {
    val chatsComponent =
        DaggerChatsComponent.builder()
            .deps(ChatsDepsProvider.deps)
            .build()
}