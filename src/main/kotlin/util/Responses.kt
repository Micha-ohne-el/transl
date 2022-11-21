package util

import dev.kord.common.Color
import dev.kord.core.behavior.interaction.response.DeferredMessageInteractionResponseBehavior
import dev.kord.core.behavior.interaction.response.respond
import dev.kord.rest.builder.message.EmbedBuilder
import dev.kord.rest.builder.message.modify.embed

suspend fun DeferredMessageInteractionResponseBehavior.respondWithError(errorEmbed: suspend EmbedBuilder.() -> Unit) =
    respond {
        embed {
            color = Color.red

            errorEmbed()
        }
    }

suspend fun DeferredMessageInteractionResponseBehavior.respondWithSuccess(errorEmbed: suspend EmbedBuilder.() -> Unit) =
    respond {
        embed {
            color = Color.green

            errorEmbed()
        }
    }

suspend fun DeferredMessageInteractionResponseBehavior.respondNeutrally(errorEmbed: suspend EmbedBuilder.() -> Unit) =
    respond {
        embed {
            color = Color.blurple

            errorEmbed()
        }
    }
