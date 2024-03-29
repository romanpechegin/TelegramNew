package com.saer.api.flows

import com.saer.api.TelegramCredentials
import com.saer.api.TelegramFlow
import com.saer.api.coroutines.checkDatabaseEncryptionKey
import com.saer.api.coroutines.setTdlibParameters
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.mapNotNull
import kotlinx.coroutines.flow.onEach
import org.drinkless.td.libcore.telegram.TdApi
import org.drinkless.td.libcore.telegram.TdApi.*

/**
 * emits [AuthorizationState] if the user authorization state has changed.
 */
fun TelegramFlow.authorizationStateFlow(): Flow<AuthorizationState> =
    this.getUpdatesFlowOfType<TdApi.UpdateAuthorizationState>()
    .mapNotNull { it.authorizationState }
        .onEach {
            when (it) {
                is AuthorizationStateWaitTdlibParameters ->
                    this.setTdlibParameters(TelegramCredentials.parameters)
                is AuthorizationStateWaitEncryptionKey ->
                    this.checkDatabaseEncryptionKey(null)
            }
        }

/**
 * emits [UpdateOption] if an option changed its value.
 */
fun TelegramFlow.optionFlow(): Flow<UpdateOption> = this.getUpdatesFlowOfType()

/**
 * emits animationIds [Int[]] if the list of saved animations was updated.
 */
fun TelegramFlow.savedAnimationsFlow(): Flow<IntArray> =
    this.getUpdatesFlowOfType<TdApi.UpdateSavedAnimations>()
    .mapNotNull { it.animationIds }

/**
 * emits [UpdateSelectedBackground] if the selected background has changed.
 */
fun TelegramFlow.selectedBackgroundFlow(): Flow<UpdateSelectedBackground> =
    this.getUpdatesFlowOfType()

/**
 * emits [UpdateLanguagePackStrings] if some language pack strings have been updated.
 */
fun TelegramFlow.languagePackStringsFlow(): Flow<UpdateLanguagePackStrings> =
    this.getUpdatesFlowOfType()

/**
 * emits state [ConnectionState] if the connection state has changed.
 */
fun TelegramFlow.connectionStateFlow(): Flow<ConnectionState> =
    this.getUpdatesFlowOfType<TdApi.UpdateConnectionState>()
    .mapNotNull { it.state }

/**
 * emits [UpdateTermsOfService] if new terms of service must be accepted by the user. If the terms
 * of service are declined, then the deleteAccount method should be called with the reason
 * &quot;Decline ToS update&quot;.
 */
fun TelegramFlow.termsOfServiceFlow(): Flow<UpdateTermsOfService> = this.getUpdatesFlowOfType()

/**
 * emits [UpdateNewInlineQuery] if a new incoming inline query; for bots only.
 */
fun TelegramFlow.newInlineQueryFlow(): Flow<UpdateNewInlineQuery> = this.getUpdatesFlowOfType()

/**
 * emits [UpdateNewChosenInlineResult] if the user has chosen a result of an inline query; for bots
 * only.
 */
fun TelegramFlow.newChosenInlineResultFlow(): Flow<UpdateNewChosenInlineResult> =
    this.getUpdatesFlowOfType()

/**
 * emits [UpdateNewShippingQuery] if a new incoming shipping query; for bots only. Only for invoices
 * with flexible price.
 */
fun TelegramFlow.newShippingQueryFlow(): Flow<UpdateNewShippingQuery> = this.getUpdatesFlowOfType()

/**
 * emits [UpdateNewPreCheckoutQuery] if a new incoming pre-checkout query; for bots only. Contains
 * full information about a checkout.
 */
fun TelegramFlow.newPreCheckoutQueryFlow(): Flow<UpdateNewPreCheckoutQuery> =
    this.getUpdatesFlowOfType()

/**
 * emits event [String] if a new incoming event; for bots only.
 */
fun TelegramFlow.newCustomEventFlow(): Flow<String> =
    this.getUpdatesFlowOfType<TdApi.UpdateNewCustomEvent>()
    .mapNotNull { it.event }

/**
 * emits [UpdateNewCustomQuery] if a new incoming query; for bots only.
 */
fun TelegramFlow.newCustomQueryFlow(): Flow<UpdateNewCustomQuery> = this.getUpdatesFlowOfType()
