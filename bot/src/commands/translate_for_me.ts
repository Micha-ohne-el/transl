import { getLogger } from "@logtape/logtape";
import { type ApplicationCommandRegistry, Command, type ContextMenuCommand } from "@sapphire/framework";
import { strings } from "../strings";
import {
	ApplicationCommandType,
	InteractionContextType,
	type MessageContextMenuCommandInteraction,
	MessageFlags,
} from "discord.js";
import { environment } from "../environment";
import { translationRepo } from "../translation_repo";
import { targetLangs } from "../utils/langs";

const logger = getLogger(["transl", "commands", "translate-for-me"]);

export class TranslateForMeCommand extends Command {
	override async registerApplicationCommands(registry: ApplicationCommandRegistry) {
		registry.registerContextMenuCommand(
			builder => {
				builder
					.setType(ApplicationCommandType.Message)
					.setName(strings.commands.translateForMe.name.original)
					.setNameLocalizations(strings.commands.translateForMe.name.getAll())
					.setContexts(InteractionContextType.Guild, InteractionContextType.BotDM, InteractionContextType.PrivateChannel);
			},
			{
				guildIds: environment.ui.discord.testGuildIds,
			},
		);
	}

	override async contextMenuRun(interaction: MessageContextMenuCommandInteraction, context: ContextMenuCommand.RunContext) {
		const log = logger.with({ id: this.runIndex++, ...context });

		log.info("#{id} Executing command {commandName} ({commandId})");
		log.debug("#{id} Deferring reply");
		const reply = await interaction.deferReply({ flags: MessageFlags.Ephemeral });

		const targetLang = targetLangs[interaction.locale];

		try {
			const translated = await translationRepo.get({ targetLang, sourceText: interaction.targetMessage.content });
			log.debug("#{id} Editing reply");
			await reply.edit(translated);
			log.info("#{id} Command successfully executed");
		} catch {
			log.error("#{id} An error occurred – replying with error message");
			await reply.edit({
				embeds: [
					{
						color: 0xed4245,
						title: strings.error.generic.title.get(interaction.locale),
						description: strings.error.generic.description.get(interaction.locale),
					},
				],
			});
			return;
		}
	}

	private runIndex = 1;
}
