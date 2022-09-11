package com.saer.api.coroutines

import com.saer.api.TelegramFlow
import org.drinkless.td.libcore.telegram.TdApi

/**
 * Suspend function, which returns all active sessions of the current user.
 *
 * @return [Sessions] Contains a list of sessions.
 */
suspend fun TelegramFlow.getActiveSessions(): TdApi.Sessions =
    this.sendFunctionAsync(TdApi.GetActiveSessions())

/**
 * Suspend function, which terminates all other sessions of the current user.
 */
suspend fun TelegramFlow.terminateAllOtherSessions() =
    this.sendFunctionLaunch(TdApi.TerminateAllOtherSessions())

/**
 * Suspend function, which terminates a session of the current user.
 *
 * @param sessionId Session identifier.
 */
suspend fun TelegramFlow.terminateSession(sessionId: Long) =
    this.sendFunctionLaunch(TdApi.TerminateSession(sessionId))
