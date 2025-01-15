import {
	DeepLError,
	Translator as DeeplTranslator,
	QuotaExceededError,
	TooManyRequestsError,
	type SourceLanguageCode,
	type TargetLanguageCode,
} from "deepl-node";
import { environment } from "./environment";
import { Memo } from "./utils/memo";

class Translator {
	async translate(
		text: string,
		sourceLang?: SourceLanguageCode | null,
		targetLang?: TargetLanguageCode | null,
	): Promise<string> {
		console.debug("Attempting translation.", { text, sourceLang, targetLang });

		try {
			const result = await this.deeplTranslator.translateText(text, sourceLang ?? null, targetLang ?? "en-US"); // TODO: Guild langs (needs config module).

			console.info("Translation result:", result);

			return result.text;
		} catch (e) {
			console.error("An error occurred during tanslation!");

			// TODO: these should be recorded in some logging tool like Sentry:
			if (e instanceof TooManyRequestsError) {
				console.error("Too many requests against the DeepL API!", e.message);
			} else if (e instanceof QuotaExceededError) {
				console.error("Translation quota exceeded!", e.message);
			} else if (e instanceof DeepLError) {
				console.error(e.name, e.message);
			}

			throw e;
		}
	}

	private deeplTranslator = new DeeplTranslator(environment.deeplAuthToken);

	readonly sourceLangs = new Memo(() => {
		console.log("Getting source languages and memoizing them for 24 hours...");

		return {
			value: this.deeplTranslator.getSourceLanguages(),
			validUntil: new Date(Date.now() + 24 * 60 * 60 * 1000), // 24 hours
		};
	});

	readonly targetLangs = new Memo(() => {
		console.log("Getting target languages and memoizing them for 24 hours...");

		return {
			value: this.deeplTranslator.getTargetLanguages(),
			validUntil: new Date(Date.now() + 24 * 60 * 60 * 1000), // 24 hours
		};
	});
}

export const translator = new Translator();
