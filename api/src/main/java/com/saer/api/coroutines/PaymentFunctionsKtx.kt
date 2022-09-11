package com.saer.api.coroutines

import com.saer.api.TelegramFlow
import org.drinkless.td.libcore.telegram.TdApi
import org.drinkless.td.libcore.telegram.TdApi.PaymentForm
import org.drinkless.td.libcore.telegram.TdApi.PaymentReceipt
import org.drinkless.td.libcore.telegram.TdApi.PaymentResult

/**
 * Suspend function, which returns an invoice payment form. This method should be called when the
 * user presses inlineKeyboardButtonBuy.
 *
 * @param chatId Chat identifier of the Invoice message.
 * @param messageId Message identifier.
 *
 * @return [PaymentForm] Contains information about an invoice payment form.
 */
//suspend fun TelegramFlow.getPaymentForm(chatId: Long, messageId: Long): PaymentForm =
//    this.sendFunctionAsync(TdApi.GetPaymentForm(chatId, messageId))

/**
 * Suspend function, which returns information about a successful payment.
 *
 * @param chatId Chat identifier of the PaymentSuccessful message.
 * @param messageId Message identifier.
 *
 * @return [PaymentReceipt] Contains information about a successful payment.
 */
suspend fun TelegramFlow.getPaymentReceipt(chatId: Long, messageId: Long): PaymentReceipt =
    this.sendFunctionAsync(TdApi.GetPaymentReceipt(chatId, messageId))

/**
 * Suspend function, which sends a filled-out payment form to the bot for final verification.
 *
 * @param chatId Chat identifier of the Invoice message.
 * @param messageId Message identifier.
 * @param orderInfoId Identifier returned by ValidateOrderInfo, or an empty string.
 * @param shippingOptionId Identifier of a chosen shipping option, if applicable.
 * @param credentials The credentials chosen by user for payment.
 *
 * @return [PaymentResult] Contains the result of a payment request.
 */
//suspend fun TelegramFlow.sendPaymentForm(
//  chatId: Long,
//  messageId: Long,
//  orderInfoId: String?,
//  shippingOptionId: String?,
//  credentials: InputCredentials?
//): PaymentResult = this.sendFunctionAsync(TdApi.SendPaymentForm(chatId, messageId, orderInfoId,
//    shippingOptionId, credentials))
