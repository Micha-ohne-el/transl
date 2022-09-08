package discord.commands

import Vault
import deepl.DeeplClient
import deepl.TargetLang
import dev.kord.core.behavior.interaction.response.respond
import discord.ChatInputCommand
import discord.params.StringParam
import discord.toTargetLang

object Translate : ChatInputCommand() {
    override val name = "translate"
    override val description = "Translate a message to the chat!"

    private val message by StringParam("The message you want to translate.")

    override suspend fun execute() {
        val guildLang = interaction.guildLocale?.toTargetLang() ?: TargetLang.BritishEnglish

        val behavior = interaction.deferPublicResponse()

        behavior.respond {
            content = deepl.translate(message, guildLang)
        }
    }


    private val deepl = DeeplClient(Vault.deeplAuthKey)
}
