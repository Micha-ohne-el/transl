package discord

import dev.kord.core.entity.interaction.ChatInputCommandInteraction
import dev.kord.rest.builder.interaction.IntegerOptionBuilder
import dev.kord.rest.builder.interaction.NumberOptionBuilder
import dev.kord.rest.builder.interaction.OptionsBuilder
import dev.kord.rest.builder.interaction.StringChoiceBuilder
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty
import util.toSnakeCase

abstract class Command {
    abstract val name: String
    abstract val description: String

    abstract suspend fun execute()

    val params = mutableListOf<Param<*>>()

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

    lateinit var interaction: ChatInputCommandInteraction


    protected class IntParam(
        private val description: String
    ) : RequiredParam<Long>() {
        override val option
        get() = IntegerOptionBuilder(name, description).apply {
            required = true
        }
    }

    protected class NumberParam(
        private val description: String
    ) : RequiredParam<Double>() {
        override val option
        get() = NumberOptionBuilder(name, description).apply {
            required = true
        }
    }

    protected class StringParam(
        private val description: String,
        private val allowedChoices: List<String>? = null
    ) : RequiredParam<String>() {
        override val option
        get() = StringChoiceBuilder(name, description).apply {
            required = true

            if (allowedChoices != null) {
                for (c in allowedChoices) {
                    choice(c, c)
                }
            }
        }
        }
    }
}
