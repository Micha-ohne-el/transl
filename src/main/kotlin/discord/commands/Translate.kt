package discord.commands

import deepl.DeepLClient
import deepl.SourceLang
import deepl.TargetLang
import dev.kord.core.behavior.interaction.response.respond
import discord.Command

object Translate : Command() {
    override val name = "translate"
    override val description = "Translate a message to the chat!"

    private val targetLang by StringParam(
        "The language you want to translate to",
        TargetLang.values().slice(0..24).map(TargetLang::name)
    )

    private val message by StringParam("The message you want to translate.")

    private val sourceLang by StringParam("Your language").Optional()

    override suspend fun execute() {
        val behavior = interaction.deferPublicResponse()

        behavior.respond {
            if (sourceLang != null)
                content = deepl.translate(message, TargetLang.valueOf(targetLang), SourceLang.valueOf(sourceLang!!))
            else
                content = deepl.translate(message, TargetLang.valueOf(targetLang))
        }
    }


    private val deepl = DeepLClient(Vault.deeplAuthKey)
}
