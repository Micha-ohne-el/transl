package discord.params.base

import discord.Command
import kotlin.reflect.KProperty

abstract class RequiredParam<T : Any> : Param<T>() {
    override operator fun getValue(
        command: Command,
        property: KProperty<*>
    ): T {
        val interactionCommand = command.interaction.command

        if (interactionCommand.options[name] == null) {
            throw Error("Required param has not been provided a value.")
        }

        @Suppress("UNCHECKED_CAST")
        return interactionCommand.options[name]?.value as T
    }
}
