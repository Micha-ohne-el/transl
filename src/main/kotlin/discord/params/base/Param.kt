package discord.params.base

import dev.kord.core.entity.interaction.AutoCompleteInteraction
import dev.kord.core.entity.interaction.ChatInputCommandInteraction
import dev.kord.rest.builder.interaction.OptionsBuilder
import discord.Command
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty
import util.toSnakeCase

abstract class Param<T> : ReadOnlyProperty<Command<out ChatInputCommandInteraction>, T> {
    operator fun provideDelegate(
        command: Command<out ChatInputCommandInteraction>,
        prop: KProperty<*>
    ): ReadOnlyProperty<Command<out ChatInputCommandInteraction>, T> {
        name = prop.name.toSnakeCase()

        command.params.add(this)

        return this
    }

    @Suppress("PARAMETER_NAME_CHANGED_ON_OVERRIDE")
    abstract override operator fun getValue(
        command: Command<out ChatInputCommandInteraction>,
        property: KProperty<*>
    ): T

    abstract suspend fun suggest(interaction: AutoCompleteInteraction)

    open val suggester: ((query: String) -> Iterable<Suggestion<T>>)? = null

    abstract val option: OptionsBuilder

    lateinit var name: String
}
