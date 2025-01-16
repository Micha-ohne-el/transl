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
	return await localize(name, { context: "A widely-recognized language spoken by many people around the world." });
}

interface LocalizeOptions {
	context?: string;
}
