import {
	DeepLError,
	Translator as DeeplTranslator,
	QuotaExceededError,
	type SourceLanguageCode,
	type TargetLanguageCode,
	TooManyRequestsError,
} from "deepl-node";
import { environment } from "./environment";

export class Translator {
	async translate({ sourceLang, targetLang, sourceText, context }: Translate): Promise<string> {
		const index = this.index++;

		console.debug(`[translation #${index}]`, "Attempting translation.", { sourceText, sourceLang, targetLang });

		try {
			const result = await this.deeplTranslator.translateText(sourceText, sourceLang ?? null, targetLang ?? "en-US", {
				formality: "prefer_less",
				preserveFormatting: true,
				context,
			});

			console.info(`[translation #${index}]`, "Translation result:", result);

			return result.text;
		} catch (e) {
			console.error(`[translation #${index}]`, "An error occurred during tanslation!");

			// TODO: these should be recorded in some logging tool like Sentry:
			if (e instanceof TooManyRequestsError) {
				console.error(`[translation #${index}]`, "Too many requests against the DeepL API!", e.message);
			} else if (e instanceof QuotaExceededError) {
				console.error(`[translation #${index}]`, "Translation quota exceeded!", e.message);
			} else if (e instanceof DeepLError) {
				console.error(`[translation #${index}]`, e.name, e.message);
			}

			throw e;
		}
	}

	private index = 1;

	private deeplTranslator = new DeeplTranslator(environment.deepl.authToken);
}

interface Translate {
	sourceLang?: SourceLanguageCode;
	targetLang: TargetLanguageCode;
	sourceText: string;
	context?: string;
}
