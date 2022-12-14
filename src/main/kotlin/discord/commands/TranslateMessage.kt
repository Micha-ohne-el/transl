package discord.commands

import GuildLangRepo
import TranslationRepo
import deepl.SourceLang.English
import dev.kord.core.behavior.interaction.response.respond
import discord.MessageCommand
import discord.toTargetLang
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import util.respondWithError
import util.toLongOrNull

private val log: Logger = LoggerFactory.getLogger("TranslateMessage")

object TranslateMessage : MessageCommand() {
    override val name = "Translate message"

    override suspend fun execute() {
        val behavior = interaction.deferEphemeralResponse()

        val guildId = interaction.data.guildId.toLongOrNull()

        val guildLang = (if (guildId != null) GuildLangRepo.getGuildLang(guildId) else null)
            ?: interaction.guildLocale?.toTargetLang()
            ?: GuildLangRepo.defaultGuildLang

        val message = interaction.messages.values.firstOrNull()

        try {
            if (message?.content.isNullOrEmpty()) {
                log.warn("Empty message could not be translated. $message")

                behavior.respondWithError {
                    field {
                        name = TranslationRepo.translate("Sorry", guildLang, English)
                        value = TranslationRepo.translate(
                            """
                                This message cannot be translated as it does not contain regular text.
                                Text in Embeds cannot yet be translated.
                                """,
                            guildLang,
                            English
                        )
                    }
                }

                return
            }

            behavior.respond {
                content = TranslationRepo.translate(message!!.content, guildLang)
            }
        } catch (error: Throwable) {
            log.error("Message could not be translated. $message")

            behavior.respondWithError {
                field {
                    name = TranslationRepo.translate("Sorry", guildLang, English)
                    value = TranslationRepo.translate(
                        """
                            An error occurred while translating that message, please try again later.
                        """,
                        guildLang,
                        English
                    )
                }
            }

            throw error
        }
    }
}
