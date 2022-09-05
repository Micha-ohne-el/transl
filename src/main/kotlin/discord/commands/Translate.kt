package discord.commands

import deepl.DeepLClient
import deepl.TargetLang
import dev.kord.core.behavior.interaction.response.respond
import discord.Command

object Translate : Command() {
    override val name = "translate"
    override val description = "Translate a message to the chat!"

    private val targetLang by StringParam("The language you want to translate to.")

    private val message by StringParam("The message you want to translate.")

    override suspend fun execute() {
        val behavior = interaction.deferPublicResponse()

        behavior.respond {
            content = deepl.translate(message, TargetLang.valueOf(targetLang))
        }
    }


    private val deepl = DeepLClient(Vault.deeplAuthKey)
}
