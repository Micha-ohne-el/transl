import type { Locale } from "discord.js";

export class LocalizedString extends Map<Locale, string> {
	constructor(public original: string) {
		super();
	}

	getAll(): Record<Locale, string> {
		return Object.fromEntries(this.entries()) as Record<Locale, string>;
	}
}
