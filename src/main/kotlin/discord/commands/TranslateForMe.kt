package discord.commands

import TranslationRepo
import deepl.SourceLang.English
import dev.kord.core.behavior.interaction.response.respond
import discord.MessageCommand
import discord.toTargetLang
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import util.respondWithError

private val log: Logger = LoggerFactory.getLogger("TranslateForMe")

object TranslateForMe : MessageCommand() {
    override val name = "Translate for me"

    override suspend fun execute() {
        val behavior = interaction.deferEphemeralResponse()

        val targetLang = interaction.locale!!.toTargetLang()

        val message = interaction.messages.values.firstOrNull()

        try {
            if (message?.content.isNullOrEmpty()) {
                log.warn("Empty message could not be translated. $message")

                behavior.respondWithError {
                    field {
                        name = TranslationRepo.translate("Sorry", targetLang, English)
                        value = TranslationRepo.translate(
                            """
                                This message cannot be translated as it does not contain regular text.
                                Text in Embeds cannot yet be translated.
                                """,
                            targetLang,
                            English
                        )
                    }
                }

                return
            }

            behavior.respond {
                content = TranslationRepo.translate(message!!.content, targetLang)
            }
        } catch (error: Throwable) {
            log.error("Message could not be translated. $message")

            behavior.respondWithError {
                field {
                    name = TranslationRepo.translate("Sorry", targetLang, English)
                    value = TranslationRepo.translate(
                        """
                            An error occurred while translating that message, please try again later.
                        """,
                        targetLang,
                        English
                    )
                }
            }

            throw error
        }
    }
}
