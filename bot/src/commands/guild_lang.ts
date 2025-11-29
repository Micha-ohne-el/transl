import { type ApplicationCommandRegistry, type ChatInputCommand, Command } from "@sapphire/framework";
import type { LanguageCode, TargetLanguageCode } from "deepl-node";
import type { ApplicationCommandOptionChoiceData, Snowflake } from "discord.js";
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
import type { LocalizedString } from "../utils/localized_string";
import { normalizeName } from "../utils/normalize_name";
import { getLogger, type Logger } from "@logtape/logtape";
import { settingsStorage } from "../settings_storage";

const logger = getLogger(["transl", "commands", "guild-lang"]);

export class GuildLangCommand extends Command {
	override async registerApplicationCommands(registry: ApplicationCommandRegistry) {
		registry.registerChatInputCommand(
			builder => {
				builder
					.setName(normalizeName(strings.commands.guildLang.name.original))
					.setNameLocalizations(strings.commands.guildLang.name.getAllNormalized())
					.setDescription(strings.commands.guildLang.description.original)
					.setDescriptionLocalizations(strings.commands.guildLang.description.getAll())
					.setContexts(InteractionContextType.Guild)
					.addSubcommand(builder =>
						builder
							.setName(normalizeName(strings.commands.guildLang.subcommands.get.name.original))
							.setNameLocalizations(strings.commands.guildLang.subcommands.get.name.getAllNormalized())
							.setDescription(strings.commands.guildLang.subcommands.get.description.original)
							.setDescriptionLocalizations(strings.commands.guildLang.subcommands.get.description.getAll()),
					)
					.addSubcommand(builder =>
						builder
							.setName(normalizeName(strings.commands.guildLang.subcommands.set.name.original))
							.setNameLocalizations(strings.commands.guildLang.subcommands.set.name.getAllNormalized())
							.setDescription(strings.commands.guildLang.subcommands.set.description.original)
							.setDescriptionLocalizations(strings.commands.guildLang.subcommands.set.description.getAll())
							.addStringOption(builder =>
								builder
									.setName(normalizeName(strings.commands.guildLang.subcommands.set.parameters.language.name.original))
									.setNameLocalizations(strings.commands.guildLang.subcommands.set.parameters.language.name.getAllNormalized())
									.setDescription(strings.commands.guildLang.subcommands.set.parameters.language.description.original)
									.setDescriptionLocalizations(
										strings.commands.guildLang.subcommands.set.parameters.language.description.getAll(),
									)
									.setAutocomplete(true)
									.setRequired(false),
							),
					);
			},
			{
				guildIds: environment.ui.discord.testGuildIds,
			},
		);
	}

	override async autocompleteRun(interaction: AutocompleteInteraction) {
		const log = logger.with({ id: this.autocompleteIndex++ });

		const focusedOption = interaction.options.getFocused(true);
		log.info("#{id} Providing autocomplete", { focusedOption });

		if (focusedOption.name === "language") {
			await interaction.respond(this.findMatchingTargetLangs(log, focusedOption.value));
		} else {
			log.warn("#{id} Autocomplete interaction received for option that doesn't have autocomplete", {
				interaction,
				focusedOption,
			});
		}
	}

	override async chatInputRun(interaction: ChatInputCommandInteraction, context: ChatInputCommand.RunContext) {
		const subcommand = interaction.options.getSubcommand(true);

		const log = logger.with({ id: this.runIndex++, subcommand, ...context });

		log.info("#{id} Executing command {commandName} ({commandId})");
		log.debug("#{id} Deferring reply");
		const reply = await interaction.deferReply({ flags: MessageFlags.Ephemeral });

		if (!interaction.guildId) {
			log.error("Command was used outside of a guild somehow");
			return;
		}

		if (subcommand === "get") {
			log.debug("Getting guild lang");
			const guildLang = await settingsStorage.getGuildLang(interaction.guildId);
			log.debug("Guild lang: {guildLang}", { guildLang });

			// TODO: translate this to guildLang:
			await reply.edit({
				embeds: [
					{
						color: 0x5865f2,
						title: strings.commands.guildLang.subcommands.get.success.title.original,
						description: guildLang ? this.targetLangs.get(guildLang)?.original : "None set (defaults to British English)",
					},
				],
			});
		} else if (subcommand === "set") {
			const guildLang = this.getTargetLangFromOption(log, interaction.options.getString("language"));

			await settingsStorage.setGuildLang(interaction.guildId, guildLang);

			// TODO: translate this to guildLang:
			await reply.edit({
				embeds: [
					{
						color: 0x5865f2,
						title: strings.commands.guildLang.subcommands.set.success.title.original,
						description: guildLang ? this.targetLangs.get(guildLang)?.original : "None",
					},
				],
			});
		} else {
			log.error("Unknown subcommand {subcommand}");
		}
	}

	private runIndex = 1;
	private autocompleteIndex = 1;

	private findMatchingTargetLangs(log: Logger, partialName: string): ApplicationCommandOptionChoiceData[] {
		log.info("#{id} Finding matching target languages for {partialName}", { partialName });

		const result = this.findMatchingLangs(log, this.targetLangs, this.targetLangFuse, partialName);

		log.info("#{id} Found {count} matches", { count: result.length, result });

		return result.slice(0, 25);
	}

	private findMatchingLangs(
		log: Logger,
		langs: Map<LanguageCode, LocalizedString>,
		fuse: Fuse<{ code: LanguageCode; localized: LocalizedString }>,
		partialName: string,
	): ApplicationCommandOptionChoiceData[] {
		if (partialName === "") {
			log.debug("#{id} Skipping Fuse search");
			const result: ApplicationCommandOptionChoiceData[] = langs
				.entries()
				.map(([code, localized]) => ({
					name: localized.original,
					value: code,
					nameLocalizations: localized.getAll(),
				}))
				.toArray();
			log.info("#{id} Returning all langs", { count: result.length, total: langs.size });
			return result;
		}

		log.debug("#{id} Querying Fuse");
		const result: ApplicationCommandOptionChoiceData[] = fuse.search(partialName).map(result => ({
			name: result.item.localized.original,
			value: result.item.code,
			nameLocalizations: result.item.localized.getAll(),
		}));
		return result;
	}

	private getTargetLangFromOption(logger: Logger, option: string | null): TargetLanguageCode | undefined {
		const log = logger.with({ option });

		log.debug("#{id} Converting option to target lang");

		if (!option) {
			log.debug("#{id} Option was blank, returning undefined");
			return undefined;
		}

		if (this.targetLangs.has(option as TargetLanguageCode)) {
			log.debug("#{id} Option is a TargetLanguageCode");

			return option as TargetLanguageCode;
		}

		log.debug("Querying Fuse");
		const result = this.targetLangFuse.search(option);
		log.debug("Found {count} matches", { count: result.length, result });
		if (result.length === 0) return undefined;

		return result[0].item.code;
	}

	private targetLangs = new Map<TargetLanguageCode, LocalizedString>([
		["ar", strings.languages.arabic],
		["bg", strings.languages.bulgarian],
		["cs", strings.languages.czeck],
		["da", strings.languages.danish],
		["de", strings.languages.german],
		["el", strings.languages.greek],
		["en-GB", strings.languages.english.british],
		["en-US", strings.languages.english.american],
		["es", strings.languages.spanish],
		["et", strings.languages.estonian],
		["fi", strings.languages.finnish],
		["fr", strings.languages.french],
		["hu", strings.languages.hungarian],
		["id", strings.languages.indonesian],
		["it", strings.languages.italian],
		["ja", strings.languages.japanese],
		["ko", strings.languages.korean],
		["lt", strings.languages.lithuanian],
		["lv", strings.languages.latvian],
		["nb", strings.languages.norwegian],
		["nl", strings.languages.dutch],
		["pl", strings.languages.polish],
		["pt-BR", strings.languages.portuguese.brazilian],
		["pt-PT", strings.languages.portuguese.generic],
		["ro", strings.languages.romanian],
		["ru", strings.languages.russian],
		["sk", strings.languages.slovak],
		["sl", strings.languages.slovenian],
		["sv", strings.languages.swedish],
		["tr", strings.languages.turkish],
		["uk", strings.languages.ukranian],
		["zh", strings.languages.chinese.generic],
	]);
	private targetLangFuse = new Fuse(
		this.targetLangs
			.entries()
			// We convert the LocalizedString to an object so that Fuse can traverse it but keep the map for all other uses.
			.map(([code, localized]) => ({ code, localized, obj: localized.getAll() }))
			.toArray(),
		{
			keys: ["code", ...Object.values(Locale).map(l => `obj.${l}`)],
			shouldSort: true,
			isCaseSensitive: false,
			ignoreDiacritics: true,
			threshold: 0.2,
		},
	);
}
