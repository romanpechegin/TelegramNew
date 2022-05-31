package com.saer.telegramnew.communications

import com.saer.telegramnew.model.Result

interface ResultCheckPhoneCommunication : Communication<Result<Boolean>> {
    class Base : Communication.Base<Result<Boolean>>(), ResultCheckPhoneCommunication
}