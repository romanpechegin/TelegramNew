package com.saer.api.coroutines

import com.saer.api.TelegramFlow
import org.drinkless.td.libcore.telegram.TdApi
import org.drinkless.td.libcore.telegram.TdApi.*

/**
 * Suspend function, which accepts an incoming call.
 *
 * @param callId Call identifier.
 * @param protocol Description of the call protocols supported by the client.
 */
suspend fun TelegramFlow.acceptCall(callId: Int, protocol: CallProtocol?) =
    this.sendFunctionLaunch(TdApi.AcceptCall(callId, protocol))

/**
 * Suspend function, which creates a new call.
 *
 * @param userId Identifier of the user to be called.
 * @param protocol Description of the call protocols supported by the client.
 *
 * @return [CallId] Contains the call identifier.
 */
suspend fun TelegramFlow.createCall(
    userId: Long,
    protocol: CallProtocol?,
    isVideo: Boolean
): CallId =
    this.sendFunctionAsync(TdApi.CreateCall(userId, protocol, isVideo))

/**
 * Suspend function, which discards a call.
 *
 * @param callId Call identifier.
 * @param isDisconnected True, if the user was disconnected.
 * @param duration The call duration, in seconds.
 * @param connectionId Identifier of the connection used during the call.
 */
suspend fun TelegramFlow.discardCall(
    callId: Int,
    isDisconnected: Boolean,
    duration: Int,
    isVideo: Boolean,
    connectionId: Long
) = this.sendFunctionLaunch(
    TdApi.DiscardCall(
        callId,
        isDisconnected,
        duration,
        isVideo,
        connectionId
    )
)

/**
 * Suspend function, which searches for call messages. Returns the results in reverse chronological
 * order (i. e., in order of decreasing messageId). For optimal performance the number of returned
 * messages is chosen by the library.
 *
 * @param fromMessageId Identifier of the message from which to search; use 0 to get results from
 * the last message.
 * @param limit The maximum number of messages to be returned; up to 100. Fewer messages may be
 * returned than specified by the limit, even if the end of the message history has not been reached.
 * @param onlyMissed If true, returns only messages with missed calls.
 *
 * @return [Messages] Contains a list of messages.
 */
suspend fun TelegramFlow.searchCallMessages(
    fromMessageId: Long,
    limit: Int,
    onlyMissed: Boolean
): Messages = this.sendFunctionAsync(TdApi.SearchCallMessages(fromMessageId, limit, onlyMissed))

/**
 * Suspend function, which sends debug information for a call.
 *
 * @param callId Call identifier.
 * @param debugInformation Debug information in application-specific format.
 */
suspend fun TelegramFlow.sendCallDebugInformation(callId: Int, debugInformation: String?) =
    this.sendFunctionLaunch(TdApi.SendCallDebugInformation(callId, debugInformation))

/**
 * Suspend function, which sends a call rating.
 *
 * @param callId Call identifier.
 * @param rating Call rating; 1-5.
 * @param comment An optional user comment if the rating is less than 5.
 * @param problems List of the exact types of problems with the call, specified by the user.
 */
suspend fun TelegramFlow.sendCallRating(
    callId: Int,
    rating: Int,
    comment: String?,
    problems: Array<CallProblem>?
) = this.sendFunctionLaunch(TdApi.SendCallRating(callId, rating, comment, problems))

/**
 * Suspend function, which returns the received bytes; for testing only. This is an offline method.
 * Can be called before authorization.
 *
 * @param x Bytes to return.
 *
 * @return [TestBytes] A simple object containing a sequence of bytes; for testing only.
 */
suspend fun TelegramFlow.testCallBytes(x: ByteArray?): TestBytes =
    this.sendFunctionAsync(TdApi.TestCallBytes(x))

/**
 * Suspend function, which does nothing; for testing only. This is an offline method. Can be called
 * before authorization.
 */
suspend fun TelegramFlow.testCallEmpty() = this.sendFunctionLaunch(TdApi.TestCallEmpty())

/**
 * Suspend function, which returns the received string; for testing only. This is an offline method.
 * Can be called before authorization.
 *
 * @param x String to return.
 *
 * @return [TestString] A simple object containing a string; for testing only.
 */
suspend fun TelegramFlow.testCallString(x: String?): TestString =
    this.sendFunctionAsync(TdApi.TestCallString(x))

/**
 * Suspend function, which returns the received vector of numbers; for testing only. This is an
 * offline method. Can be called before authorization.
 *
 * @param x Vector of numbers to return.
 *
 * @return [TestVectorInt] A simple object containing a vector of numbers; for testing only.
 */
suspend fun TelegramFlow.testCallVectorInt(x: IntArray?): TestVectorInt =
    this.sendFunctionAsync(TdApi.TestCallVectorInt(x))

/**
 * Suspend function, which returns the received vector of objects containing a number; for testing
 * only. This is an offline method. Can be called before authorization.
 *
 * @param x Vector of objects to return.
 *
 * @return [TestVectorIntObject] A simple object containing a vector of objects that hold a number;
 * for testing only.
 */
suspend fun TelegramFlow.testCallVectorIntObject(x: Array<TestInt>?): TestVectorIntObject =
    this.sendFunctionAsync(TdApi.TestCallVectorIntObject(x))

/**
 * Suspend function, which returns the received vector of strings; for testing only. This is an
 * offline method. Can be called before authorization.
 *
 * @param x Vector of strings to return.
 *
 * @return [TestVectorString] A simple object containing a vector of strings; for testing only.
 */
suspend fun TelegramFlow.testCallVectorString(x: Array<String>?): TestVectorString =
    this.sendFunctionAsync(TdApi.TestCallVectorString(x))

/**
 * Suspend function, which returns the received vector of objects containing a string; for testing
 * only. This is an offline method. Can be called before authorization.
 *
 * @param x Vector of objects to return.
 *
 * @return [TestVectorStringObject] A simple object containing a vector of objects that hold a
 * string; for testing only.
 */
suspend fun TelegramFlow.testCallVectorStringObject(x: Array<TestString>?): TestVectorStringObject =
    this.sendFunctionAsync(TdApi.TestCallVectorStringObject(x))
