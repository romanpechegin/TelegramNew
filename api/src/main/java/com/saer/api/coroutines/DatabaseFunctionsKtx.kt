package com.saer.api.coroutines

import com.saer.api.TelegramFlow
import org.drinkless.td.libcore.telegram.TdApi
import org.drinkless.td.libcore.telegram.TdApi.DatabaseStatistics

/**
 * Suspend function, which checks the database encryption key for correctness. Works only when the
 * current authorization state is authorizationStateWaitEncryptionKey.
 *
 * @param encryptionKey Encryption key to check or set up.
 */
suspend fun TelegramFlow.checkDatabaseEncryptionKey(encryptionKey: ByteArray?) =
    this.sendFunctionLaunch(TdApi.CheckDatabaseEncryptionKey(encryptionKey))

/**
 * Suspend function, which returns database statistics.
 *
 * @return [DatabaseStatistics] Contains database statistics.
 */
suspend fun TelegramFlow.getDatabaseStatistics(): DatabaseStatistics =
    this.sendFunctionAsync(TdApi.GetDatabaseStatistics())

/**
 * Suspend function, which changes the database encryption key. Usually the encryption key is never
 * changed and is stored in some OS keychain.
 *
 * @param newEncryptionKey New encryption key.
 */
suspend fun TelegramFlow.setDatabaseEncryptionKey(newEncryptionKey: ByteArray?) =
    this.sendFunctionLaunch(TdApi.SetDatabaseEncryptionKey(newEncryptionKey))