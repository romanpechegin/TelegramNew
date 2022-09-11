package com.saer.login.mappers

import com.saer.core.Mapper
import com.saer.login.ui.EnterPhoneUi
import org.drinkless.td.libcore.telegram.TdApi
import org.drinkless.td.libcore.telegram.TdApi.AuthorizationState

interface MapperAuthorisationStateToEnterPhoneUi : Mapper<AuthorizationState, EnterPhoneUi> {
    class Base : MapperAuthorisationStateToEnterPhoneUi {
        override fun map(data: AuthorizationState): EnterPhoneUi {
            return when (data) {
                is TdApi.AuthorizationStateReady -> EnterPhoneUi.WaitEnterPhoneUi()
                is TdApi.AuthorizationStateWaitCode -> EnterPhoneUi.NavigateToEnterCodeUi()
                is TdApi.AuthorizationStateWaitPassword -> EnterPhoneUi.WaitEnterPhoneUi()
                is TdApi.AuthorizationStateWaitPhoneNumber -> EnterPhoneUi.WaitEnterPhoneUi()
                is TdApi.AuthorizationStateWaitTdlibParameters -> EnterPhoneUi.WaitEnterPhoneUi()
                is TdApi.AuthorizationStateWaitEncryptionKey -> EnterPhoneUi.WaitEnterPhoneUi()
                else -> EnterPhoneUi.ErrorPhoneUi(Throwable(data.javaClass.simpleName))
            }
        }
    }
}