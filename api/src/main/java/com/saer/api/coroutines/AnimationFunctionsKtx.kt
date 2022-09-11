package com.saer.api.coroutines

import com.saer.api.TelegramFlow
import org.drinkless.td.libcore.telegram.TdApi
import org.drinkless.td.libcore.telegram.TdApi.Animations
import org.drinkless.td.libcore.telegram.TdApi.InputFile

/**
 * Suspend function, which manually adds a new animation to the list of saved animations. The new
 * animation is added to the beginning of the list. If the animation was already in the list, it is
 * removed first. Only non-secret video animations with MIME type &quot;video/mp4&quot; can be added to
 * the list.
 *
 * @param animation The animation file to be added. Only animations known to the server (i.e.
 * successfully sent via a message) can be added to the list.
 */
suspend fun TelegramFlow.addSavedAnimation(animation: InputFile?) =
    this.sendFunctionLaunch(TdApi.AddSavedAnimation(animation))

/**
 * Suspend function, which returns saved animations.
 *
 * @return [Animations] Represents a list of animations.
 */
suspend fun TelegramFlow.getSavedAnimations(): Animations =
    this.sendFunctionAsync(TdApi.GetSavedAnimations())

/**
 * Suspend function, which removes an animation from the list of saved animations.
 *
 * @param animation Animation file to be removed.
 */
suspend fun TelegramFlow.removeSavedAnimation(animation: InputFile?) =
    this.sendFunctionLaunch(TdApi.RemoveSavedAnimation(animation))
