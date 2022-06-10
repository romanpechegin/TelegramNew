package com.saer.telegramnew.communications

import com.saer.telegramnew.ui.EnterCodeUi

interface EnterCodeUiCommunication : Communication<EnterCodeUi> {
    class Base : Communication.Base<EnterCodeUi>(), EnterCodeUiCommunication
}