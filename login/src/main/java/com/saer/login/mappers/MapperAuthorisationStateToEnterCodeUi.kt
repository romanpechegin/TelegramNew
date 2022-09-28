package com.saer.login.mappers

import com.saer.core.mappers.Mapper
import com.saer.login.ui.EnterCodeUi
import org.drinkless.td.libcore.telegram.TdApi
import org.drinkless.td.libcore.telegram.TdApi.AuthorizationState

interface MapperAuthorisationStateToEnterCodeUi : Mapper<AuthorizationState, EnterCodeUi> {
    class Base : MapperAuthorisationStateToEnterCodeUi {
        override fun map(data: AuthorizationState): EnterCodeUi {
            return when (data) {
                is TdApi.AuthorizationStateReady -> EnterCodeUi.SuccessAuthUi()
                is TdApi.AuthorizationStateWaitPassword -> EnterCodeUi.NavigateToEnterPasswordUi()
                is TdApi.AuthorizationStateWaitCode -> {
                    EnterCodeUi.WaitCodeUi(data.codeInfo.phoneNumber)
                }
                is TdApi.AuthorizationStateWaitPhoneNumber -> EnterCodeUi.WaitCodeUi()
                is TdApi.AuthorizationStateWaitTdlibParameters -> EnterCodeUi.WaitCodeUi()
                is TdApi.AuthorizationStateWaitEncryptionKey -> EnterCodeUi.WaitCodeUi()
                is TdApi.AuthorizationStateWaitRegistration -> EnterCodeUi.NavigateToRegistrationUi()
                else -> EnterCodeUi.ErrorCodeUi(IllegalStateException())
            }
        }
    }
}