package com.saer.telegramnew.auth.communication

import com.saer.telegramnew.auth.ui.EnterPhoneUi
import com.saer.telegramnew.common.Communication

interface EnterPhoneUiCommunication : Communication<EnterPhoneUi> {
    class Base : Communication.Base<EnterPhoneUi>(), EnterPhoneUiCommunication
}