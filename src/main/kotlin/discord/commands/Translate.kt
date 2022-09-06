package discord.commands

import deepl.DeeplClient
import deepl.TargetLang
import dev.kord.core.behavior.interaction.response.respond
import discord.Command

object Translate : Command() {
    override val name = "translate"
    override val description = "Translate a message to the chat!"

    private val message by StringParam("The message you want to translate.")

    private val guildLang = TargetLang.AmericanEnglish

    override suspend fun execute() {
        val behavior = interaction.deferPublicResponse()

        behavior.respond {
            content = deepl.translate(message, guildLang)
        }
    }


    private val deepl = DeeplClient(Vault.deeplAuthKey)
}
