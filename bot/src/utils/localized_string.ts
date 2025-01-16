import type { Locale } from "discord.js";
import { normalizeName } from "./normalize_name";

export class LocalizedString extends Map<Locale, string> {
	constructor(public original: string) {
		super();
	}

	getAll(): Record<Locale, string> {
		return Object.fromEntries(this.entries()) as Record<Locale, string>;
	}

	getAllNormalized(): Record<Locale, string> {
		return Object.fromEntries(this.entries().map(([k, v]) => [k, normalizeName(v)])) as Record<Locale, string>;
	}
}
