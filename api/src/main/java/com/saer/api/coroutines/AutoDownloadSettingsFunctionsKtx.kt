package com.saer.api.coroutines

import com.saer.api.TelegramFlow
import org.drinkless.td.libcore.telegram.TdApi
import org.drinkless.td.libcore.telegram.TdApi.*

/**
 * Suspend function, which returns auto-download settings presets for the currently logged in user.
 *
 * @return [AutoDownloadSettingsPresets] Contains auto-download settings presets for the user.
 */
suspend fun TelegramFlow.getAutoDownloadSettingsPresets(): AutoDownloadSettingsPresets =
    this.sendFunctionAsync(TdApi.GetAutoDownloadSettingsPresets())

/**
 * Suspend function, which sets auto-download settings.
 *
 * @param settings New user auto-download settings.  
 * @param type Type of the network for which the new settings are applied.
 */
suspend fun TelegramFlow.setAutoDownloadSettings(settings: AutoDownloadSettings?,
    type: NetworkType?) = this.sendFunctionLaunch(TdApi.SetAutoDownloadSettings(settings, type))
