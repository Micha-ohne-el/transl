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
import { normalizeName } from "../utils/normalize_name";
import { getLogger, type Logger } from "@logtape/logtape";

const logger = getLogger(["transl", "commands", "translate"]);

export class TranslateCommand extends Command {
	public constructor(context: Command.LoaderContext, options: Command.Options) {
		super(context, {
			...options,
			name: normalizeName(strings.commands.translate.name.original),
			description: strings.commands.translate.description.original,
		});
	}

	public override async registerApplicationCommands(registry: ApplicationCommandRegistry) {
		registry.registerChatInputCommand(
			builder => {
				builder
					.setName(normalizeName(strings.commands.translate.name.original))
					.setNameLocalizations(strings.commands.translate.name.getAllNormalized())
					.setDescription(strings.commands.translate.description.original)
					.setDescriptionLocalizations(strings.commands.translate.description.getAll())
					.setContexts(InteractionContextType.Guild, InteractionContextType.BotDM, InteractionContextType.PrivateChannel)
					.addStringOption(option =>
						option
							.setName(normalizeName(strings.commands.translate.parameters.message.name.original))
							.setNameLocalizations(strings.commands.translate.parameters.message.name.getAllNormalized())
							.setDescription(strings.commands.translate.parameters.message.description.original)
							.setDescriptionLocalizations(strings.commands.translate.parameters.message.description.getAll())
							.setRequired(true),
					)
					.addStringOption(option =>
						option
							.setName(normalizeName(strings.commands.translate.parameters.sourceLang.name.original))
							.setNameLocalizations(strings.commands.translate.parameters.sourceLang.name.getAllNormalized())
							.setDescription(strings.commands.translate.parameters.sourceLang.description.original)
							.setDescriptionLocalizations(strings.commands.translate.parameters.sourceLang.description.getAll())
							.setAutocomplete(true)
							.setRequired(false),
					)
					.addStringOption(option =>
						option
							.setName(normalizeName(strings.commands.translate.parameters.targetLang.name.original))
							.setNameLocalizations(strings.commands.translate.parameters.targetLang.name.getAllNormalized())
							.setDescription(strings.commands.translate.parameters.targetLang.description.original)
							.setDescriptionLocalizations(strings.commands.translate.parameters.targetLang.description.getAll())
							.setAutocomplete(true)
							.setRequired(false),
					);
			},
			{
				guildIds: environment.ui.discord.testGuildIds,
			},
		);
	}

	public override async autocompleteRun(interaction: AutocompleteInteraction) {
		const log = logger.with({ id: this.autocompleteIndex++ });

		const focusedOption = interaction.options.getFocused(true);
		log.info("Providing autocomplete", { focusedOption });

		if (focusedOption.name === "source_language") {
			await interaction.respond(this.findMatchingLanguages(log, this.sourceLangs, focusedOption.value));
		} else if (focusedOption.name === "target_language") {
			await interaction.respond(this.findMatchingLanguages(log, this.targetLangs, focusedOption.value));
		} else {
			log.warn("Autocomplete interaction received for option that doesn't have autocomplete", {
				interaction,
				focusedOption,
			});
		}
	}

	public override async chatInputRun(interaction: ChatInputCommandInteraction) {
		const log = logger.with({ id: this.runIndex++, command: "/translate" });

		log.info("Executing command {command}");
		log.debug("Deferring reply");
		const reply = await interaction.deferReply();

		const message = interaction.options.getString("message", true);
		const sourceLang = (interaction.options.getString("source_language") as SourceLanguageCode | null) ?? undefined;
		const targetLang = (interaction.options.getString("target_language") as TargetLanguageCode | null) ?? "en-US"; // todo: guild langs.

		log.info("Retreived command options", { message, sourceLang, targetLang });

		try {
			log.debug("Editing reply");
			await reply.edit(await translationRepo.get({ sourceLang, targetLang, sourceText: message }));
		} catch (e) {
			log.error("An error occurred – deleting reply and sending error message");
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

	private runIndex = 1;
	private autocompleteIndex = 1;

	private findMatchingLanguages(
		log: Logger,
		languages: Partial<Record<LanguageCode, LocalizedString>>,
		partialName: string,
	): ApplicationCommandOptionChoiceData[] {
		log.info("Finding matching languages for {partialName}", { partialName, languages });

		if (partialName === "") {
			log.debug("Skipping Fuse initialization");
			const result: ApplicationCommandOptionChoiceData[] = [...Object.entries(languages)]
				.slice(0, 25)
				.map(([code, localized]) => ({
					name: localized.original,
					value: code,
					nameLocalizations: localized.getAll(),
				}));
			log.info("Found {count} matches", { count: result.length, result });
			return result;
		}

		log.debug("Initializing Fuse");
		const fuse = new Fuse(
			[...Object.entries(languages)].map(([code, localized]) => ({ code, localized })),
			{
				keys: ["code", Object.values(Locale).map(l => `localized.${l}`)],
				shouldSort: true,
				isCaseSensitive: false,
				ignoreDiacritics: true,
				findAllMatches: true,
			},
		);

		log.debug("Querying Fuse");
		const result: ApplicationCommandOptionChoiceData[] = fuse.search(partialName, { limit: 5 }).map(result => ({
			name: result.item.localized.original,
			value: result.item.code,
			nameLocalizations: result.item.localized.getAll(),
		}));
		log.info("Found {count} matches", { count: result.length, result });
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
