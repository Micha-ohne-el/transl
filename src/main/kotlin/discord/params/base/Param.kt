package discord.params.base

import dev.kord.rest.builder.interaction.OptionsBuilder
import discord.Command
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty
import util.toSnakeCase

abstract class Param<T> : ReadOnlyProperty<Command, T> {
    operator fun provideDelegate(
        command: Command,
        prop: KProperty<*>
    ): ReadOnlyProperty<Command, T> {
        name = prop.name.toSnakeCase()

        command.params.add(this)

        return this
    }

    abstract override operator fun getValue(
        command: Command,
        property: KProperty<*>
    ): T

    abstract val option: OptionsBuilder


    protected lateinit var name: String
}
