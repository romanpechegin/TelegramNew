package com.saer.telegramnew.communications

import com.saer.telegramnew.ui.EnterPhoneUi

interface EnterPhoneUiCommunication : Communication<EnterPhoneUi> {
    class Base : Communication.Base<EnterPhoneUi>(), EnterPhoneUiCommunication
}