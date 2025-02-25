import * as Deepl from "deepl-node";
import type { DeeplTranslatorConfig, TranslatorConfig } from "../../Config";

const logger = getLogger(["transl", "translator"]);

export function getTranslator(config: TranslatorConfig): Translator {
	if ("deepl" in config && config.deepl) {
		return new DeeplTranslator(config.deepl);
	}
	throw new Error("Translator config is invalid!");
}

export interface Translator {
	translate(options: Translate): Promise<string>;
}

export class DeeplTranslator implements Translator {
	constructor(config: DeeplTranslatorConfig) {
		this.deeplTranslator = new Deepl.Translator(config.authToken);
	}

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
		} catch (error) {
			if (error instanceof Deepl.TooManyRequestsError) {
				console.error(`[translation #${index}]`, "Too many requests against the DeepL API!", error.message);
			} else if (error instanceof Deepl.QuotaExceededError) {
				console.error(`[translation #${index}]`, "Translation quota exceeded!", error.message);
			} else if (error instanceof Deepl.DeepLError) {
				console.error(`[translation #${index}]`, error.name, error.message);
			}

			throw error;
		}
	}

	private readonly deeplTranslator: Deepl.Translator;

	private index = 1;
}

interface Translate {
	sourceLang?: Deepl.SourceLanguageCode;
	targetLang: Deepl.TargetLanguageCode;
	sourceText: string;
	context?: string;
}
