package discord

import dev.kord.core.entity.interaction.ChatInputCommandInteraction
import discord.params.base.Param

abstract class Command {
    abstract val name: String
    abstract val description: String

    abstract suspend fun execute()

    val params = mutableListOf<Param<*>>()

    lateinit var interaction: ChatInputCommandInteraction
}
