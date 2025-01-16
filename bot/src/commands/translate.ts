import { type ApplicationCommandRegistry, Command } from "@sapphire/framework";
import type { LanguageCode, SourceLanguageCode, TargetLanguageCode } from "deepl-node";
import type { ApplicationCommandOptionChoiceData } from "discord.js";
import {
	type AutocompleteInteraction,
	type ChatInputCommandInteraction,
	InteractionContextType,
	MessageFlags,
} from "discord.js";
import { Locale } from "discord.js";
import Fuse from "fuse.js";
import { environment } from "../environment";
import { strings } from "../strings";
import { translationRepo } from "../translation_repo";
import type { LocalizedString } from "../utils/localized_string";

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
			await interaction.respond(this.findMatchingLanguages(this.sourceLangs, focusedOption.value));
		} else if (focusedOption.name === "target_language") {
			await interaction.respond(this.findMatchingLanguages(this.targetLangs, focusedOption.value));
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
			await reply.edit(await translationRepo.get(sourceLang, targetLang, message));
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

	private findMatchingLanguages(
		languages: Partial<Record<LanguageCode, LocalizedString>>,
		partialName: string,
	): ApplicationCommandOptionChoiceData[] {
		console.debug("Finding matching languages.", { partialName, languages });

		if (partialName === "") {
			const result: ApplicationCommandOptionChoiceData[] = [...Object.entries(languages)]
				.slice(0, 25)
				.map(([code, localized]) => ({
					name: localized.original,
					value: code,
					nameLocalizations: localized.getAll(),
				}));
			console.debug("Result:", result);
			return result;
		}

		const fuse = new Fuse(
			[...Object.entries(languages)].map(([code, localized]) => ({ code, localized })),
			{ keys: ["code", Object.values(Locale).map(l => `localized.${l}`)] },
		);

		const result: ApplicationCommandOptionChoiceData[] = fuse.search(partialName, { limit: 5 }).map(result => ({
			name: result.item.localized.original,
			value: result.item.code,
			nameLocalizations: result.item.localized.getAll(),
		}));
		console.debug("Result:", result);
		return result;
	}

	private sourceLangs: Record<SourceLanguageCode, LocalizedString> = {
		ar: strings.languages.arabic,
		bg: strings.languages.bulgarian,
		cs: strings.languages.czeck,
		da: strings.languages.danish,
		de: strings.languages.german,
		el: strings.languages.greek,
		en: strings.languages.english.generic,
		es: strings.languages.spanish,
		et: strings.languages.estonian,
		fi: strings.languages.finnish,
		fr: strings.languages.french,
		hu: strings.languages.hungarian,
		id: strings.languages.indonesian,
		it: strings.languages.italian,
		ja: strings.languages.japanese,
		ko: strings.languages.korean,
		lt: strings.languages.lithuanian,
		lv: strings.languages.latvian,
		nb: strings.languages.norwegian,
		nl: strings.languages.dutch,
		pl: strings.languages.polish,
		pt: strings.languages.portuguese.generic,
		ro: strings.languages.romanian,
		ru: strings.languages.russian,
		sk: strings.languages.slovak,
		sl: strings.languages.slovenian,
		sv: strings.languages.swedish,
		tr: strings.languages.turkish,
		uk: strings.languages.ukranian,
		zh: strings.languages.chinese.generic,
	};

	private targetLangs: Record<TargetLanguageCode, LocalizedString> = {
		ar: strings.languages.arabic,
		bg: strings.languages.bulgarian,
		cs: strings.languages.czeck,
		da: strings.languages.danish,
		de: strings.languages.german,
		el: strings.languages.greek,
		"en-GB": strings.languages.english.british,
		"en-US": strings.languages.english.american,
		es: strings.languages.spanish,
		et: strings.languages.estonian,
		fi: strings.languages.finnish,
		fr: strings.languages.french,
		hu: strings.languages.hungarian,
		id: strings.languages.indonesian,
		it: strings.languages.italian,
		ja: strings.languages.japanese,
		ko: strings.languages.korean,
		lt: strings.languages.lithuanian,
		lv: strings.languages.latvian,
		nb: strings.languages.norwegian,
		nl: strings.languages.dutch,
		pl: strings.languages.polish,
		"pt-BR": strings.languages.portuguese.brazilian,
		"pt-PT": strings.languages.portuguese.generic,
		ro: strings.languages.romanian,
		ru: strings.languages.russian,
		sk: strings.languages.slovak,
		sl: strings.languages.slovenian,
		sv: strings.languages.swedish,
		tr: strings.languages.turkish,
		uk: strings.languages.ukranian,
		zh: strings.languages.chinese.generic,
	};
}
