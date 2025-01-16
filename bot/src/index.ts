import { SapphireClient } from "@sapphire/framework";
import { environment } from "./environment";

const client = new SapphireClient({
	intents: [],
});

await client.login(environment.discord.botToken);
