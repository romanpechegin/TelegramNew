package com.saer.api.extensions

import com.saer.api.TelegramFlow

/**
 * Base extensions interface
 */
interface BaseKtx {
  /**
   * Instance of the [TelegramFlow] connecting extensions to the Telegram Client
   */
  val api: TelegramFlow
}
