export function normalizeName(name: string): string {
	return name.toLowerCase().replaceAll(/[^\p{Ll}\p{Lm}\p{Lo}\p{N}\p{sc=Devanagari}\p{sc=Thai}_-]+/gu, "_");
}
