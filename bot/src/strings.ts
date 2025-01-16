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
		arabic: await localize("Arabic"),
		bulgarian: await localize("Bulgarian"),
		czeck: await localize("Czeck"),
		danish: await localize("Danish"),
		german: await localize("German"),
		greek: await localize("Greek"),
		english: {
			generic: await localize("English"),
			american: await localize("American English"),
			british: await localize("British English"),
		},
		spanish: await localize("Spanish"),
		estonian: await localize("Estonian"),
		finnish: await localize("Finnish"),
		french: await localize("French"),
		hungarian: await localize("Hungarian"),
		indonesian: await localize("Indonesian"),
		italian: await localize("Italian"),
		japanese: await localize("Japanese"),
		korean: await localize("Korean"),
		lithuanian: await localize("Lithuanian"),
		latvian: await localize("Latvian"),
		norwegian: await localize("Norwegian"),
		dutch: await localize("Dutch"),
		polish: await localize("Polish"),
		portuguese: {
			generic: await localize("Portuguese"),
			brazilian: await localize("Brazilian Portuguese"),
		},
		romanian: await localize("Romanian"),
		russian: await localize("Russian"),
		slovak: await localize("Slovak"),
		slovenian: await localize("Slovenian"),
		swedish: await localize("Swedish"),
		turkish: await localize("Turkish"),
		ukranian: await localize("Ukranian"),
		chinese: {
			generic: await localize("Chinese"),
			traditional: await localize("Traditional Chinese"),
			simplified: await localize("Simplified Chinese"),
		},
	},
};

async function localize(text: string): Promise<LocalizedString> {
	const map = new LocalizedString(text);

	await Promise.all(
		Object.values(Locale).map(async locale => {
			map.set(locale, await translationRepo.get("en", targetLangs[locale], text));
		}),
	);

	return map;
}
