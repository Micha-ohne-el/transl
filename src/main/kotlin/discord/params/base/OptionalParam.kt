package discord.params.base

import discord.Command
import kotlin.reflect.KProperty

abstract class OptionalParam<T> : Param<T?>() {
    override operator fun getValue(
        command: Command,
        property: KProperty<*>
    ): T? {
        val interactionCommand = command.interaction.command

        @Suppress("UNCHECKED_CAST")
        return interactionCommand.options[name]?.value as? T
    }
}
