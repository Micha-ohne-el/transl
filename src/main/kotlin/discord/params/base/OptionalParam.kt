package discord.params.base

import dev.kord.core.entity.interaction.ChatInputCommandInteraction
import discord.Command
import kotlin.reflect.KProperty

abstract class OptionalParam<T> : Param<T?>() {
    override operator fun getValue(
        command: Command<out ChatInputCommandInteraction>,
        property: KProperty<*>
    ): T? {
        val interactionCommand = command.interaction.command

        @Suppress("UNCHECKED_CAST")
        return interactionCommand.options[name]?.value as? T
    }
}
