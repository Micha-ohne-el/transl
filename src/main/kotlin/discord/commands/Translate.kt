package discord.commands

import deepl.DeepLClient
import deepl.TargetLang
import dev.kord.core.behavior.interaction.response.respond
import dev.kord.core.entity.interaction.ApplicationCommandInteraction
import discord.Command

object Translate : Command {
    override val name = "translate"
    override val description = "Translate a message to the chat!"

    override suspend fun execute(interaction: ApplicationCommandInteraction) {
        val behavior = interaction.deferPublicResponse()

        behavior.respond {
            content = deepl.translate("This is a test.", TargetLang.Portuguese)
        }
    }


    private val deepl = DeepLClient(Vault.deeplAuthKey)
}
