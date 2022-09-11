package com.saer.api.coroutines

import com.saer.api.TelegramFlow
import org.drinkless.td.libcore.telegram.TdApi
import org.drinkless.td.libcore.telegram.TdApi.*

/**
 * Suspend function, which accepts Telegram terms of services.
 *
 * @param termsOfServiceId Terms of service identifier.
 */
suspend fun TelegramFlow.acceptTermsOfService(termsOfServiceId: String?) =
    this.sendFunctionLaunch(TdApi.AcceptTermsOfService(termsOfServiceId))

/**
 * Suspend function, which checks whether the current session can be used to transfer a chat
 * ownership to another user.
 *
 * @return [CanTransferOwnershipResult] This class is an abstract base class.
 */
suspend fun TelegramFlow.canTransferOwnership(): CanTransferOwnershipResult =
    this.sendFunctionAsync(TdApi.CanTransferOwnership())

/**
 * Suspend function, which closes the TDLib instance. All databases will be flushed to disk and
 * properly closed. After the close completes, updateAuthorizationState with authorizationStateClosed
 * will be sent.
 */
suspend fun TelegramFlow.close() = this.sendFunctionLaunch(TdApi.Close())

/**
 * Suspend function, which deletes saved credentials for all payment provider bots.
 */
suspend fun TelegramFlow.deleteSavedCredentials() =
    this.sendFunctionLaunch(TdApi.DeleteSavedCredentials())

/**
 * Suspend function, which closes the TDLib instance, destroying all local data without a proper
 * logout. The current user session will remain in the list of all active sessions. All local data will
 * be destroyed. After the destruction completes updateAuthorizationState with authorizationStateClosed
 * will be sent.
 */
suspend fun TelegramFlow.destroy() = this.sendFunctionLaunch(TdApi.Destroy())

/**
 * Suspend function, which disconnects all websites from the current user's Telegram account.
 */
suspend fun TelegramFlow.disconnectAllWebsites() =
    this.sendFunctionLaunch(TdApi.DisconnectAllWebsites())

/**
 * Suspend function, which disconnects website from the current user's Telegram account.
 *
 * @param websiteId Website identifier.
 */
suspend fun TelegramFlow.disconnectWebsite(websiteId: Long) =
    this.sendFunctionLaunch(TdApi.DisconnectWebsite(websiteId))

/**
 * Suspend function, which returns application config, provided by the server. Can be called before
 * authorization.
 *
 * @return [JsonValue] This class is an abstract base class.
 */
suspend fun TelegramFlow.getApplicationConfig(): JsonValue =
    this.sendFunctionAsync(TdApi.GetApplicationConfig())

/**
 * Suspend function, which returns the current authorization state; this is an offline request. For
 * informational purposes only. Use updateAuthorizationState instead to maintain the current
 * authorization state.
 *
 * @return [AuthorizationState] This class is an abstract base class.
 */
suspend fun TelegramFlow.getAuthorizationState(): AuthorizationState =
    this.sendFunctionAsync(TdApi.GetAuthorizationState())

/**
 * Suspend function, which returns all website where the current user used Telegram to log in.
 *
 * @return [ConnectedWebsites] Contains a list of websites the current user is logged in with
 * Telegram.
 */
suspend fun TelegramFlow.getConnectedWebsites(): ConnectedWebsites =
    this.sendFunctionAsync(TdApi.GetConnectedWebsites())

/**
 * Suspend function, which returns all updates needed to restore current TDLib state, i.e. all
 * actual UpdateAuthorizationState/UpdateUser/UpdateNewChat and others. This is especially useful if
 * TDLib is run in a separate process. This is an offline method. Can be called before authorization.
 *
 * @return [Updates] Contains a list of updates.
 */
suspend fun TelegramFlow.getCurrentState(): Updates =
    this.sendFunctionAsync(TdApi.GetCurrentState())

/**
 * Suspend function, which returns information about a tg:// deep link. Use
 * &quot;tg://need_update_for_some_feature&quot; or &quot;tg:someUnsupportedFeature&quot; for testing.
 * Returns a 404 error for unknown links. Can be called before authorization.
 *
 * @param link The link.
 *
 * @return [DeepLinkInfo] Contains information about a tg:// deep link.
 */
suspend fun TelegramFlow.getDeepLinkInfo(link: String?): DeepLinkInfo =
    this.sendFunctionAsync(TdApi.GetDeepLinkInfo(link))

/**
 * Suspend function, which returns a list of common group chats with a given user. Chats are sorted
 * by their type and creation date.
 *
 * @param userId User identifier.
 * @param offsetChatId Chat identifier starting from which to return chats; use 0 for the first
 * request.
 * @param limit The maximum number of chats to be returned; up to 100.
 *
 * @return [Chats] Represents a list of chats.
 */
suspend fun TelegramFlow.getGroupsInCommon(
  userId: Long,
  offsetChatId: Long,
  limit: Int
): Chats = this.sendFunctionAsync(TdApi.GetGroupsInCommon(userId, offsetChatId, limit))

///**
// * Suspend function, which returns the default text for invitation messages to be used as a
// * placeholder when the current user invites friends to Telegram.
// *
// * @return [Text] Contains some text.
// */
//suspend fun TelegramFlow.getInviteText(): Text = this.sendFunctionAsync(GetInviteText())

/**
 * Suspend function, which converts a JsonValue object to corresponding JSON-serialized string. This
 * is an offline method. Can be called before authorization. Can be called synchronously.
 *
 * @param jsonValue The JsonValue object.
 *
 * @return [Text] Contains some text.
 */
suspend fun TelegramFlow.getJsonString(jsonValue: JsonValue?): Text =
    this.sendFunctionAsync(TdApi.GetJsonString(jsonValue))

/**
 * Suspend function, which converts a JSON-serialized string to corresponding JsonValue object. This
 * is an offline method. Can be called before authorization. Can be called synchronously.
 *
 * @param json The JSON-serialized string.
 *
 * @return [JsonValue] This class is an abstract base class.
 */
suspend fun TelegramFlow.getJsonValue(json: String?): JsonValue =
    this.sendFunctionAsync(TdApi.GetJsonValue(json))

/**
 * Suspend function, which returns information about the current localization target. This is an
 * offline request if onlyLocal is true. Can be called before authorization.
 *
 * @param onlyLocal If true, returns only locally available information without sending network
 * requests.
 *
 * @return [LocalizationTargetInfo] Contains information about the current localization target.
 */
suspend fun TelegramFlow.getLocalizationTargetInfo(onlyLocal: Boolean): LocalizationTargetInfo =
    this.sendFunctionAsync(TdApi.GetLocalizationTargetInfo(onlyLocal))

/**
 * Suspend function, which returns the current user.
 *
 * @return [User] Represents a user.
 */
suspend fun TelegramFlow.getMe(): User = this.sendFunctionAsync(TdApi.GetMe())

/**
 * Suspend function, which returns the value of an option by its name. (Check the list of available
 * options on https://core.telegram.org/tdlib/options.) Can be called before authorization.
 *
 * @param name The name of the option.
 *
 * @return [OptionValue] This class is an abstract base class.
 */
suspend fun TelegramFlow.getOption(name: String?): OptionValue =
    this.sendFunctionAsync(TdApi.GetOption(name))

/**
 * Suspend function, which returns list of proxies that are currently set up. Can be called before
 * authorization.
 *
 * @return [Proxies] Represents a list of proxy servers.
 */
suspend fun TelegramFlow.getProxies(): Proxies = this.sendFunctionAsync(TdApi.GetProxies())

/**
 * Suspend function, which returns a globally unique push notification subscription identifier for
 * identification of an account, which has received a push notification. This is an offline method. Can
 * be called before authorization. Can be called synchronously.
 *
 * @param payload JSON-encoded push notification payload.
 *
 * @return [PushReceiverId] Contains a globally unique push receiver identifier, which can be used
 * to identify which account has received a push notification.
 */
suspend fun TelegramFlow.getPushReceiverId(payload: String?): PushReceiverId =
    this.sendFunctionAsync(TdApi.GetPushReceiverId(payload))

/**
 * Suspend function, which returns up to 20 recently used inline bots in the order of their last
 * usage.
 *
 * @return [Users] Represents a list of users.
 */
suspend fun TelegramFlow.getRecentInlineBots(): Users =
    this.sendFunctionAsync(TdApi.GetRecentInlineBots())

/**
 * Suspend function, which returns t.me URLs recently visited by a newly registered user.
 *
 * @param referrer Google Play referrer to identify the user.
 *
 * @return [TMeUrls] Contains a list of t.me URLs.
 */
suspend fun TelegramFlow.getRecentlyVisitedTMeUrls(referrer: String?): TMeUrls =
    this.sendFunctionAsync(TdApi.GetRecentlyVisitedTMeUrls(referrer))

/**
 * Suspend function, which closes the TDLib instance after a proper logout. Requires an available
 * network connection. All local data will be destroyed. After the logout completes,
 * updateAuthorizationState with authorizationStateClosed will be sent.
 */
suspend fun TelegramFlow.logOut() = this.sendFunctionLaunch(TdApi.LogOut())

///**
// * Suspend function, which optimizes storage usage, i.e. deletes some files and returns new storage
// * usage statistics. Secret thumbnails can't be deleted.
// *
// * @param size Limit on the total size of files after deletion. Pass -1 to use the default limit.
// * @param ttl Limit on the time that has passed since the last time a file was accessed (or creation
// * time for some filesystems). Pass -1 to use the default limit.
// * @param count Limit on the total count of files after deletion. Pass -1 to use the default limit.
// *
// * @param immunityDelay The amount of time after the creation of a file during which it can't be
// * deleted, in seconds. Pass -1 to use the default value.
// * @param fileTypes If not empty, only files with the given type(s) are considered. By default, all
// * types except thumbnails, profile photos, stickers and wallpapers are deleted.
// * @param chatIds If not empty, only files from the given chats are considered. Use 0 as chat
// * identifier to delete files not belonging to any chat (e.g., profile photos).
// * @param excludeChatIds If not empty, files from the given chats are excluded. Use 0 as chat
// * identifier to exclude all files not belonging to any chat (e.g., profile photos).
// * @param chatLimit Same as in getStorageStatistics. Affects only returned statistics.
// *
// * @return [StorageStatistics] Contains the exact storage usage statistics split by chats and file
// * type.
// */
//suspend fun TelegramFlow.optimizeStorage(
//  size: Long,
//  ttl: Int,
//  count: Int,
//  immunityDelay: Int,
//  fileTypes: Array<FileType>?,
//  chatIds: LongArray?,
//  excludeChatIds: LongArray?,
//  chatLimit: Int
//): StorageStatistics = this.sendFunctionAsync(TdApi.OptimizeStorage(size, ttl, count, immunityDelay,
//    fileTypes, chatIds, excludeChatIds, chatLimit))

///**
// * Suspend function, which registers the currently used device for receiving push notifications.
// * Returns a globally unique identifier of the push notification subscription.
// *
// * @param deviceToken Device token.
// * @param otherUserIds List of user identifiers of other users currently using the client.
// *
// * @return [PushReceiverId] Contains a globally unique push receiver identifier, which can be used
// * to identify which account has received a push notification.
// */
//suspend fun TelegramFlow.registerDevice(deviceToken: DeviceToken?, otherUserIds: IntArray?):
//    PushReceiverId = this.sendFunctionAsync(TdApi.RegisterDevice(deviceToken, otherUserIds))

/**
 * Suspend function, which removes a hashtag from the list of recently used hashtags.
 *
 * @param hashtag Hashtag to delete.
 */
suspend fun TelegramFlow.removeRecentHashtag(hashtag: String?) =
    this.sendFunctionLaunch(TdApi.RemoveRecentHashtag(hashtag))

/**
 * Suspend function, which searches for recently used hashtags by their prefix.
 *
 * @param prefix Hashtag prefix to search for.
 * @param limit The maximum number of hashtags to be returned.
 *
 * @return [Hashtags] Contains a list of hashtags.
 */
suspend fun TelegramFlow.searchHashtags(prefix: String?, limit: Int): Hashtags =
    this.sendFunctionAsync(TdApi.SearchHashtags(prefix, limit))

/**
 * Suspend function, which sends a custom request; for bots only.
 *
 * @param method The method name.
 * @param parameters JSON-serialized method parameters.
 *
 * @return [CustomRequestResult] Contains the result of a custom request.
 */
suspend fun TelegramFlow.sendCustomRequest(method: String?, parameters: String?):
    CustomRequestResult = this.sendFunctionAsync(TdApi.SendCustomRequest(method, parameters))

/**
 * Suspend function, which succeeds after a specified amount of time has passed. Can be called
 * before authorization. Can be called before initialization.
 *
 * @param seconds Number of seconds before the function returns.
 */
suspend fun TelegramFlow.setAlarm(seconds: Double) =
    this.sendFunctionLaunch(TdApi.SetAlarm(seconds))

/**
 * Suspend function, which changes the bio of the current user.
 *
 * @param bio The new value of the user bio; 0-70 characters without line feeds.
 */
suspend fun TelegramFlow.setBio(bio: String?) = this.sendFunctionLaunch(TdApi.SetBio(bio))

/**
 * Suspend function, which changes the first and last name of the current user. If something
 * changes, updateUser will be sent.
 *
 * @param firstName The new value of the first name for the user; 1-64 characters.
 * @param lastName The new value of the optional last name for the user; 0-64 characters.
 */
suspend fun TelegramFlow.setName(firstName: String?, lastName: String?) =
    this.sendFunctionLaunch(TdApi.SetName(firstName, lastName))

/**
 * Suspend function, which sets the value of an option. (Check the list of available options on
 * https://core.telegram.org/tdlib/options.) Only writable options can be set. Can be called before
 * authorization.
 *
 * @param name The name of the option.
 * @param value The new value of the option.
 */
suspend fun TelegramFlow.setOption(name: String?, value: OptionValue?) =
    this.sendFunctionLaunch(TdApi.SetOption(name, value))

/**
 * Suspend function, which sets the parameters for TDLib initialization. Works only when the current
 * authorization state is authorizationStateWaitTdlibParameters.
 *
 * @param parameters Parameters.
 */
suspend fun TelegramFlow.setTdlibParameters(parameters: TdlibParameters?) =
    this.sendFunctionLaunch(TdApi.SetTdlibParameters(parameters))

/**
 * Suspend function, which forces an updates.getDifference call to the Telegram servers; for testing
 * only.
 */
suspend fun TelegramFlow.testGetDifference() = this.sendFunctionLaunch(TdApi.TestGetDifference())

/**
 * Suspend function, which returns the specified error and ensures that the Error object is used;
 * for testing only. This is an offline method. Can be called before authorization. Can be called
 * synchronously.
 *
 * @param error The error to be returned.
 *
 * @return [Error] An object of this type can be returned on every function call, in case of an
 * error.
 */
suspend fun TelegramFlow.testReturnError(error: Error?): Error =
    this.sendFunctionAsync(TdApi.TestReturnError(error))

/**
 * Suspend function, which returns the squared received number; for testing only. This is an offline
 * method. Can be called before authorization.
 *
 * @param x Number to square.
 *
 * @return [TestInt] A simple object containing a number; for testing only.
 */
suspend fun TelegramFlow.testSquareInt(x: Int): TestInt =
    this.sendFunctionAsync(TdApi.TestSquareInt(x))
