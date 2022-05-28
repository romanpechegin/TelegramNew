package com.saer.telegramnew.communication

import com.saer.telegramnew.model.Result

interface ResultCommunication : Communication<Result<Any>> {
    class Base : Communication.Base<Result<Any>>(), ResultCommunication
}