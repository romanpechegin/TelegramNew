package com.saer.telegramnew.communications

import com.saer.telegramnew.model.Result

interface ResultSendCodeCommunication : Communication<Result<Boolean>> {

    class Base : Communication.Base<Result<Boolean>>(), ResultSendCodeCommunication

}