package discord

import dev.kord.common.entity.Permission
import dev.kord.core.Kord
import dev.kord.core.entity.application.GlobalApplicationCommand
import dev.kord.core.entity.application.GuildApplicationCommand
import dev.kord.core.entity.interaction.ApplicationCommandInteraction
import discord.params.base.Param

abstract class Command<Interaction : ApplicationCommandInteraction> {
    abstract val name: String

    abstract suspend fun execute()

    abstract suspend fun makeGlobalCommand(kord: Kord): GlobalApplicationCommand
    abstract suspend fun makeGuildCommand(kord: Kord, guildId: Long): GuildApplicationCommand

    val params = mutableListOf<Param<*>>()

    open val permissions = setOf<Permission>(Permission.UseApplicationCommands)

    lateinit var interaction: Interaction
}
