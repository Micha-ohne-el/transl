package discord.commands

import TranslationRepo
import dev.kord.core.behavior.interaction.response.respond
import discord.MessageCommand
import discord.toTargetLang

object TranslateForMe : MessageCommand() {
    override val name = "Translate for me"

    override suspend fun execute() {
        val behavior = interaction.deferEphemeralResponse()

        behavior.respond {
            content = TranslationRepo.translate(
                interaction.messages.values.first().content,
                interaction.locale!!.toTargetLang()
            )
        }
    }
}
