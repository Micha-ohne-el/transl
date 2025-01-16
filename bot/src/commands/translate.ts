import { type ApplicationCommandRegistry, Command } from "@sapphire/framework";
import type { Language, SourceLanguageCode, TargetLanguageCode } from "deepl-node";
import type { ApplicationCommandOptionChoiceData } from "discord.js";
import {
	type AutocompleteInteraction,
	type ChatInputCommandInteraction,
	InteractionContextType,
	MessageFlags,
} from "discord.js";
import Fuse from "fuse.js";
import { environment } from "../environment";
import { translator } from "../translator";
import { strings } from "../strings";

export class TranslateCommand extends Command {
	public constructor(context: Command.LoaderContext, options: Command.Options) {
		super(context, {
			...options,
			name: "translate",
			description: "Translate a message to the chat!",
		});
	}

	public override async registerApplicationCommands(registry: ApplicationCommandRegistry) {
		registry.registerChatInputCommand(
			builder => {
				builder
					.setName(this.name)
					.setDescription(this.description)
					.setContexts(InteractionContextType.Guild, InteractionContextType.BotDM, InteractionContextType.PrivateChannel)
					.addStringOption(option =>
						option //
							.setName("message")
							.setDescription("The text you want to translate.")
							.setRequired(true),
					)
					.addStringOption(option =>
						option
							.setName("source_language")
							.setDescription("Your language (automatically detected if omitted).")
							.setAutocomplete(true)
							.setRequired(false),
					)
					.addStringOption(option =>
						option
							.setName("target_language")
							.setDescription("The language you want to translate to (the Server's language if omitted).")
							.setAutocomplete(true)
							.setRequired(false),
					);
			},
			{
				guildIds: environment.discord.testGuildIds,
			},
		);
	}

	public override async autocompleteRun(interaction: AutocompleteInteraction) {
		const focusedOption = interaction.options.getFocused(true);

		if (focusedOption.name === "source_language") {
			await interaction.respond(this.findMatchingLanguages(await translator.sourceLangs.value, focusedOption.value));
		} else if (focusedOption.name === "target_language") {
			await interaction.respond(this.findMatchingLanguages(await translator.targetLangs.value, focusedOption.value));
		} else {
			console.warn("Autocomplete interaction received for option that doesn't have autocomplete.", {
				interaction,
				focusedOption,
			});
		}
	}

	public override async chatInputRun(interaction: ChatInputCommandInteraction) {
		console.debug("Executing command: /translate");
		const reply = await interaction.deferReply();

		const message = interaction.options.getString("message", true);
		const sourceLang = interaction.options.getString("source_language") as SourceLanguageCode;
		const targetLang = interaction.options.getString("target_language") as TargetLanguageCode;

		console.debug("Options:", { message, sourceLang, targetLang });

		try {
			await reply.edit(await translator.translate(message, sourceLang, targetLang));
		} catch (e) {
			await Promise.all([
				reply.delete(),
				interaction.followUp({
					flags: MessageFlags.Ephemeral,
					embeds: [
						{
							color: 0xed4245,
							title: strings.error.generic.title.get(interaction.locale),
							description: strings.error.generic.description.get(interaction.locale),
						},
					],
				}),
			]);
			return;
		}
	}

	private findMatchingLanguages(languages: readonly Language[], partialName: string): ApplicationCommandOptionChoiceData[] {
		console.debug("Finding matching languages.", { partialName, languages });

		if (partialName === "") {
			const result = languages.slice(0, 25).map(l => ({
				name: l.name,
				value: l.code,
			}));
			console.debug("Result:", result);
			return result;
		}

		const fuse = new Fuse(languages, { keys: ["name", "code"] satisfies (keyof Language)[] });

		const result = fuse.search(partialName, { limit: 5 }).map(result => ({
			name: result.item.name,
			value: result.item.code,
		}));
		console.debug("Result:", result);
		return result;
	}
}
