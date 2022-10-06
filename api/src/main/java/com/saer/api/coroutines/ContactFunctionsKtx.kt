package com.saer.api.coroutines

import com.saer.api.TelegramFlow
import org.drinkless.td.libcore.telegram.TdApi
import org.drinkless.td.libcore.telegram.TdApi.*

/**
 * Suspend function, which adds a user to the contact list or edits an existing contact by their
 * user identifier.
 *
 * @param contact The contact to add or edit; phone number can be empty and needs to be specified
 * only if known, vCard is ignored.
 * @param sharePhoneNumber True, if the new contact needs to be allowed to see current user's phone
 * number. A corresponding rule to userPrivacySettingShowPhoneNumber will be added if needed. Use the
 * field UserFullInfo.needPhoneNumberPrivacyException to check whether the current user needs to be
 * asked to share their phone number.
 */
suspend fun TelegramFlow.addContact(contact: Contact?, sharePhoneNumber: Boolean) =
    this.sendFunctionLaunch(TdApi.AddContact(contact, sharePhoneNumber))

/**
 * Suspend function, which changes imported contacts using the list of current user contacts saved
 * on the device. Imports newly added contacts and, if at least the file database is enabled, deletes
 * recently deleted contacts. Query result depends on the result of the previous query, so only one
 * query is possible at the same time.
 *
 * @param contacts The new list of contacts, contact's vCard are ignored and are not imported.
 *
 * @return [ImportedContacts] Represents the result of an ImportContacts request.
 */
suspend fun TelegramFlow.changeImportedContacts(contacts: Array<Contact>?): ImportedContacts =
    this.sendFunctionAsync(TdApi.ChangeImportedContacts(contacts))

/**
 * Suspend function, which clears all imported contacts, contact list remains unchanged.
 */
suspend fun TelegramFlow.clearImportedContacts() =
    this.sendFunctionLaunch(TdApi.ClearImportedContacts())

/**
 * Suspend function, which returns all user contacts.
 *
 * @return [Users] Represents a list of users.
 */
suspend fun TelegramFlow.getContacts(): Users = this.sendFunctionAsync(TdApi.GetContacts())

/**
 * Suspend function, which adds new contacts or edits existing contacts by their phone numbers;
 * contacts' user identifiers are ignored.
 *
 * @param contacts The list of contacts to import or edit; contacts' vCard are ignored and are not
 * imported.
 *
 * @return [ImportedContacts] Represents the result of an ImportContacts request.
 */
suspend fun TelegramFlow.importContacts(contacts: Array<Contact>?): ImportedContacts =
    this.sendFunctionAsync(TdApi.ImportContacts(contacts))

/**
 * Suspend function, which removes users from the contact list.
 *
 * @param userIds Identifiers of users to be deleted.
 */
suspend fun TelegramFlow.removeContacts(userIds: LongArray?) =
    this.sendFunctionLaunch(TdApi.RemoveContacts(userIds))

/**
 * Suspend function, which searches for the specified query in the first names, last names and
 * usernames of the known user contacts.
 *
 * @param query Query to search for; may be empty to return all contacts.
 * @param limit The maximum number of users to be returned.
 *
 * @return [Users] Represents a list of users.
 */
suspend fun TelegramFlow.searchContacts(query: String?, limit: Int): Users =
    this.sendFunctionAsync(TdApi.SearchContacts(query, limit))
