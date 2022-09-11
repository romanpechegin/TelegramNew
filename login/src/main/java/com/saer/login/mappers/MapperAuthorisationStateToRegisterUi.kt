package com.saer.login.mappers

import com.saer.core.Mapper
import com.saer.login.ui.RegisterUi
import org.drinkless.td.libcore.telegram.TdApi
import org.drinkless.td.libcore.telegram.TdApi.AuthorizationState

interface MapperAuthorisationStateToRegisterUi : Mapper<AuthorizationState, RegisterUi> {
    class Base : MapperAuthorisationStateToRegisterUi {
        override fun map(data: AuthorizationState): RegisterUi {
            return when (data) {
                is TdApi.AuthorizationStateReady -> RegisterUi.SuccessRegisterUi()
                is TdApi.AuthorizationStateWaitRegistration -> RegisterUi.WaitEnterNameUi()
                is TdApi.AuthorizationStateWaitTdlibParameters -> RegisterUi.WaitEnterNameUi()
                is TdApi.AuthorizationStateWaitEncryptionKey -> RegisterUi.WaitEnterNameUi()
                else -> RegisterUi.ErrorUi(Throwable(data.javaClass.simpleName))
            }
        }
    }
}