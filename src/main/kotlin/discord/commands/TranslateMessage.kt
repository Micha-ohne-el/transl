package discord.commands

import deepl.DeeplClient
import deepl.TargetLang
import dev.kord.core.behavior.interaction.response.respond
import discord.MessageCommand

object TranslateMessage : MessageCommand() {
    override val name = "translate"
    override val description = "Translate a previously sent message!"

    private val guildLang = TargetLang.AmericanEnglish

    override suspend fun execute() {
        val behavior = interaction.deferEphemeralResponse()

        behavior.respond {
            content = deepl.translate(interaction.messages.values.first().content, guildLang)
        }
    }


    private val deepl = DeeplClient(Vault.deeplAuthKey)
}
