package com.saer.api.coroutines

import com.saer.api.TelegramFlow
import org.drinkless.td.libcore.telegram.TdApi
import org.drinkless.td.libcore.telegram.TdApi.*

/**
 * Suspend function, which uses current user IP to found their country. Returns two-letter ISO
 * 3166-1 alpha-2 country code. Can be called before authorization.
 *
 * @return [Text] Contains some text.
 */
suspend fun TelegramFlow.getCountryCode(): Text = this.sendFunctionAsync(TdApi.GetCountryCode())

suspend fun TelegramFlow.getCountries(): Countries = this.sendFunctionAsync(TdApi.GetCountries())

/**
 * Suspend function, which returns the total number of imported contacts.
 *
 * @return [Count] Contains a counter.
 */
suspend fun TelegramFlow.getImportedContactCount(): Count =
    this.sendFunctionAsync(TdApi.GetImportedContactCount())
