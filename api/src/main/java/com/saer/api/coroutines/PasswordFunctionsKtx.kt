package com.saer.api.coroutines

import com.saer.api.TelegramFlow
import org.drinkless.td.libcore.telegram.TdApi
import org.drinkless.td.libcore.telegram.TdApi.*

/**
 * Suspend function, which checks the authentication password for correctness. Works only when the
 * current authorization state is authorizationStateWaitPassword.
 *
 * @param password The password to check.
 */
suspend fun TelegramFlow.checkAuthenticationPassword(password: String?) =
    this.sendFunctionLaunch(TdApi.CheckAuthenticationPassword(password))

/**
 * Suspend function, which creates a new temporary password for processing payments.
 *
 * @param password Persistent user password.
 * @param validFor Time during which the temporary password will be valid, in seconds; should be
 * between 60 and 86400.
 *
 * @return [TemporaryPasswordState] Returns information about the availability of a temporary
 * password, which can be used for payments.
 */
suspend fun TelegramFlow.createTemporaryPassword(password: String?, validFor: Int):
    TemporaryPasswordState = this.sendFunctionAsync(TdApi.CreateTemporaryPassword(password,
    validFor))

/**
 * Suspend function, which returns the current state of 2-step verification.
 *
 * @return [PasswordState] Represents the current state of 2-step verification.
 */
suspend fun TelegramFlow.getPasswordState(): PasswordState =
    this.sendFunctionAsync(TdApi.GetPasswordState())

/**
 * Suspend function, which returns information about the current temporary password.
 *
 * @return [TemporaryPasswordState] Returns information about the availability of a temporary
 * password, which can be used for payments.
 */
suspend fun TelegramFlow.getTemporaryPasswordState(): TemporaryPasswordState =
    this.sendFunctionAsync(TdApi.GetTemporaryPasswordState())

///**
// * Suspend function, which recovers the password with a password recovery code sent to an email
// * address that was previously set up. Works only when the current authorization state is
// * authorizationStateWaitPassword.
// *
// * @param recoveryCode Recovery code to check.
// */
//suspend fun TelegramFlow.recoverAuthenticationPassword(recoveryCode: String?) =
//    this.sendFunctionLaunch(TdApi.RecoverAuthenticationPassword(recoveryCode))

///**
// * Suspend function, which recovers the password using a recovery code sent to an email address that
// * was previously set up.
// *
// * @param recoveryCode Recovery code to check.
// *
// * @return [PasswordState] Represents the current state of 2-step verification.
// */
//suspend fun TelegramFlow.recoverPassword(recoveryCode: String?): PasswordState =
//    this.sendFunctionAsync(TdApi.RecoverPassword(recoveryCode))

/**
 * Suspend function, which requests to send a password recovery code to an email address that was
 * previously set up. Works only when the current authorization state is
 * authorizationStateWaitPassword.
 */
suspend fun TelegramFlow.requestAuthenticationPasswordRecovery() =
    this.sendFunctionLaunch(TdApi.RequestAuthenticationPasswordRecovery())

/**
 * Suspend function, which requests to send a password recovery code to an email address that was
 * previously set up.
 *
 * @return [EmailAddressAuthenticationCodeInfo] Information about the email address authentication
 * code that was sent.
 */
suspend fun TelegramFlow.requestPasswordRecovery(): EmailAddressAuthenticationCodeInfo =
    this.sendFunctionAsync(TdApi.RequestPasswordRecovery())

/**
 * Suspend function, which changes the password for the user. If a new recovery email address is
 * specified, then the change will not be applied until the new recovery email address is confirmed.
 *
 * @param oldPassword Previous password of the user.
 * @param newPassword New password of the user; may be empty to remove the password.
 * @param newHint New password hint; may be empty.
 * @param setRecoveryEmailAddress Pass true if the recovery email address should be changed.
 * @param newRecoveryEmailAddress New recovery email address; may be empty.
 *
 * @return [PasswordState] Represents the current state of 2-step verification.
 */
suspend fun TelegramFlow.setPassword(
  oldPassword: String?,
  newPassword: String?,
  newHint: String?,
  setRecoveryEmailAddress: Boolean,
  newRecoveryEmailAddress: String?
): PasswordState = this.sendFunctionAsync(TdApi.SetPassword(oldPassword, newPassword, newHint,
    setRecoveryEmailAddress, newRecoveryEmailAddress))
