package com.saer.telegramnew.auth.communication

import com.saer.telegramnew.auth.ui.EnterPasswordUi
import com.saer.telegramnew.common.Communication

interface EnterPasswordUiCommunication : Communication<EnterPasswordUi> {
    class Base : Communication.Base<EnterPasswordUi>(), EnterPasswordUiCommunication
}