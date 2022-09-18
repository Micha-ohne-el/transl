package discord.commands

import GuildLangRepo
import deepl.TargetLang
import dev.kord.common.entity.Permission
import dev.kord.core.behavior.interaction.response.respond
import discord.ChatInputCommand
import discord.params.StringParam
import util.enumValueOfOrNull

object SetGuildLang : ChatInputCommand() {
    override val name = "Set language"
    override val description = "Set this Server's preferred language."

    override val permissions = setOf(Permission.ManageGuild)

    private val langName by StringParam(
        "The language people should talk in.",
        TargetLang.values().map(TargetLang::name)
    )

    private val lang get() = enumValueOfOrNull<TargetLang>(langName)

    override suspend fun execute() {
        val behavior = interaction.deferEphemeralResponse()

        if (lang == null) {
            behavior.respond {
                content = "Language $langName is not (yet) supported, unfortunately."
            }
            return
        }

        val guildIdSnowflake = interaction.data.guildId.value
        val guildId = guildIdSnowflake?.value?.toLong()

        GuildLangRepo.setGuildLang(guildId!!, lang!!)

        behavior.respond {
            content = "Language set to $lang."
        }
    }
}
