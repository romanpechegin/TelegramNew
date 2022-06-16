package com.saer.telegramnew.auth.communication

import com.saer.telegramnew.auth.ui.EnterCodeUi
import com.saer.telegramnew.common.Communication

interface EnterCodeUiCommunication : Communication<EnterCodeUi> {
    class Base : Communication.Base<EnterCodeUi>(), EnterCodeUiCommunication
}