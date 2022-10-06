package com.saer.telegramnew.ui

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.saer.api.TelegramFlow
import com.saer.api.coroutines.getChat
import com.saer.api.coroutines.getChats
import com.saer.api.coroutines.loadChats
import com.saer.core.di.IoDispatcher
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.launch
import org.drinkless.td.libcore.telegram.TdApi.ChatListMain

class ListOfChatsViewModel @AssistedInject constructor(
    @IoDispatcher ioDispatcher: CoroutineDispatcher,
    api: TelegramFlow
) : ViewModel() {

    init {
        viewModelScope.launch {
            api.loadChats()
            api.getChats(ChatListMain(), 50).chatIds.first {
                Log.e("TAG", api.getChat(it).toString())
                true
            }
        }
    }

    @AssistedFactory
    interface Factory {
        fun create(): ListOfChatsViewModel
    }
}