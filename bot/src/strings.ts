import { Locale } from "discord.js";
import { translationRepo } from "./translation_repo";
import { targetLangs } from "./utils/langs";
import { LocalizedString } from "./utils/localized_string";

export const strings = {
	error: {
		generic: {
			title: await localize("Sorry"),
			description: await localize("An error occurred, please try again later."),
		},
	},
	commands: {
		translate: {
			name: await localize("Translate", { context: "To communicate with foreigners, I need to..." }),
			description: await localize("Translate a message to the chat!"),
			parameters: {
				message: {
					name: await localize("Message", { context: "The message you want to translate." }),
					description: await localize("The message you want to translate."),
				},
				sourceLang: {
					name: await localize("Source Language", {
						context: "The language your original message is written in before being translated into the target language.",
					}),
					description: await localize("Your language (automatically detected if omitted)."),
				},
				targetLang: {
					name: await localize("Target Language", {
						context: "The language your message (which was written in the source language) will be translated to.",
					}),
					description: await localize("The language you want to translate to (the Server's language if omitted)."),
				},
			},
		},
	},
	languages: {
		arabic: await localizeLanguageName("Arabic"),
		bulgarian: await localizeLanguageName("Bulgarian"),
		czeck: await localizeLanguageName("Czeck"),
		danish: await localizeLanguageName("Danish"),
		german: await localizeLanguageName("German"),
		greek: await localizeLanguageName("Greek"),
		english: {
			generic: await localizeLanguageName("English"),
			american: await localizeLanguageName("American English"),
			british: await localizeLanguageName("British English"),
		},
		spanish: await localizeLanguageName("Spanish"),
		estonian: await localizeLanguageName("Estonian"),
		finnish: await localizeLanguageName("Finnish"),
		french: await localizeLanguageName("French"),
		hungarian: await localizeLanguageName("Hungarian"),
		indonesian: await localizeLanguageName("Indonesian"),
		italian: await localizeLanguageName("Italian"),
		japanese: await localizeLanguageName("Japanese"),
		korean: await localizeLanguageName("Korean"),
		lithuanian: await localizeLanguageName("Lithuanian"),
		latvian: await localizeLanguageName("Latvian"),
		norwegian: await localizeLanguageName("Norwegian"),
		dutch: await localizeLanguageName("Dutch"),
		polish: await localizeLanguageName("Polish"),
		portuguese: {
			generic: await localizeLanguageName("Portuguese"),
			brazilian: await localizeLanguageName("Brazilian Portuguese"),
		},
		romanian: await localizeLanguageName("Romanian"),
		russian: await localizeLanguageName("Russian"),
		slovak: await localizeLanguageName("Slovak"),
		slovenian: await localizeLanguageName("Slovenian"),
		swedish: await localizeLanguageName("Swedish"),
		turkish: await localizeLanguageName("Turkish"),
		ukranian: await localizeLanguageName("Ukranian"),
		chinese: {
			generic: await localizeLanguageName("Chinese"),
			traditional: await localizeLanguageName("Traditional Chinese"),
			simplified: await localizeLanguageName("Simplified Chinese"),
		},
	},
};

async function localize(text: string, options?: LocalizeOptions): Promise<LocalizedString> {
	const map = new LocalizedString(text);

	await Promise.all(
		Object.values(Locale).map(async locale => {
			map.set(
				locale,
				await translationRepo.get({
					sourceLang: "en",
					targetLang: targetLangs[locale],
					sourceText: text,
					cacheTimeToLiveSeconds: null,
					sourceTextContext: options?.context,
				}),
			);
		}),
	);

	return map;
}

async function localizeLanguageName(name: string) {
	return await localize(name, { context: "A spoken language." });
}

interface LocalizeOptions {
	context?: string;
}
