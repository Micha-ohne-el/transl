import { Client, Events } from "discord.js";

const client = new Client({
	intents: [],
});

client.once(Events.ClientReady, client => {
	console.log(`Logged in as ${client.user.tag}.`);
});

await client.login(process.env.DISCORD_BOT_TOKEN);
