package com.saer.telegramnew.auth.communication

import com.saer.telegramnew.auth.ui.RegisterUi
import com.saer.telegramnew.common.Communication

interface RegistrationUiCommunication : Communication<RegisterUi> {
    class Base : Communication.Base<RegisterUi>(), RegistrationUiCommunication
}