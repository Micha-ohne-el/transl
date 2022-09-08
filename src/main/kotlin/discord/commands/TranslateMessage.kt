package discord.commands

import GuildLangRepo
import Vault
import deepl.DeeplClient
import dev.kord.core.behavior.interaction.response.respond
import discord.MessageCommand
import discord.toTargetLang

object TranslateMessage : MessageCommand() {
    override val name = "translate"
    override val description = "Translate a previously sent message!"

    override suspend fun execute() {
        val guildIdSnowflake = Translate.interaction.data.guildId.value
        val guildId = guildIdSnowflake?.value?.toLong()

        val guildLang = (if (guildId != null) GuildLangRepo.getGuildLang(guildId) else null)
            ?: Translate.interaction.guildLocale?.toTargetLang()
            ?: GuildLangRepo.defaultGuildLang

        val behavior = interaction.deferEphemeralResponse()

        behavior.respond {
            content = deepl.translate(interaction.messages.values.first().content, guildLang)
        }
    }


    private val deepl = DeeplClient(Vault.deeplAuthKey)
}
