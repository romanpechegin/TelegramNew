package com.saer.api.extensions

import com.saer.api.TelegramFlow
import com.saer.api.coroutines.*
import org.drinkless.td.libcore.telegram.TdApi
import org.drinkless.td.libcore.telegram.TdApi.*

/**
 * Interface for access common
 */
interface CommonKtx : BaseKtx {
  /**
   * Instance of the [TelegramFlow] connecting extensions to the Telegram Client
   */
  override val api: TelegramFlow

  /**
   * Suspend function, which removes background from the list of installed backgrounds.
   */
  suspend fun Background.remove() = api.removeBackground(this.id)

  /**
   * Suspend function, which removes an active notification from notification list. Needs to be
   * called only if the notification is removed by the current user.
   *
   * @param notificationGroupId Identifier of notification group to which the notification belongs. 
   * 
   */
  suspend fun Notification.remove(notificationGroupId: Int) =
      api.removeNotification(notificationGroupId, this.id)

  /**
   * Suspend function, which deletes a profile photo. If something changes, updateUser will be sent.
   */
  suspend fun ProfilePhoto.delete() = api.deleteProfilePhoto(this.id)

  /**
   * Suspend function, which returns information about a file by its remote ID; this is an offline
   * request. Can be used to register a URL as a file for further uploading, or sending as a message.
   * Even the request succeeds, the file can be used only if it is still accessible to the user. For
   * example, if the file is from a message, then the message must be not deleted and accessible to the
   * user. If the file database is disabled, then the corresponding object with the file must be
   * preloaded by the client.
   *
   * @param fileType File type, if known.
   *
   * @return [TdApi.File] Represents a file.
   */
  suspend fun RemoteFile.get(fileType: FileType?) = api.getRemoteFile(this.id, fileType)

  /**
   * Suspend function, which terminates a session of the current user.
   */
  suspend fun Session.terminate() = api.terminateSession(this.id)

//  /**
//   * Suspend function, which sends a filled-out payment form to the bot for final verification.
//   *
//   * @param chatId Chat identifier of the Invoice message.
//   * @param messageId Message identifier.
//   * @param orderInfoId Identifier returned by ValidateOrderInfo, or an empty string.
//   * @param credentials The credentials chosen by user for payment.
//   *
//   * @return [TdApi.PaymentResult] Contains the result of a payment request.
//   */
//  suspend fun ShippingOption.sendPaymentForm(
//    chatId: Long,
//    messageId: Long,
//    orderInfoId: String?,
//    credentials: InputCredentials?
//  ) = api.sendPaymentForm(chatId, messageId, orderInfoId, this.id, credentials)

//  /**
//   * Suspend function, which changes the sticker set of a supergroup; requires canChangeInfo rights.
//   *
//   * @param supergroupId Identifier of the supergroup.
//   */
//  suspend fun StickerSet.setSupergroup(supergroupId: Int) =
//      api.setSupergroupStickerSet(supergroupId, this.id)
}
