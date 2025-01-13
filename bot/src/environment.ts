import { raise } from "./utils/raise";

export const environment = {
	botToken: process.env.DISCORD_BOT_TOKEN ?? raise("DISCORD_BOT_TOKEN is unset!"),
	clientId: process.env.DISCORD_CLIENT_ID ?? raise("DISCORD_CLIENT_ID is unset!"),
	testGuildId: process.env.DISCORD_TEST_GUILD ?? raise("DISCORD_TEST_GUILD is unset!"),
};
