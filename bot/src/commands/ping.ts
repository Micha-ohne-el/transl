import { type ApplicationCommandRegistry, Command } from "@sapphire/framework";
import { environment } from "../environment";

export class PingCommand extends Command {
	public constructor(context: Command.LoaderContext, options: Command.Options) {
		super(context, {
			...options,
			name: "ping",
			description: "pong?",
		});
	}

	public override async registerApplicationCommands(registry: ApplicationCommandRegistry) {
		registry.registerChatInputCommand(
			builder => {
				builder //
					.setName(this.name)
					.setDescription(this.description);
			},
			{
				guildIds: [environment.testGuildId],
			},
		);
	}

	public override async chatInputRun(interaction: Command.ChatInputCommandInteraction) {
		const message = await interaction.reply({
			content: "Measuring...",
			withResponse: true,
		});

		const roundTripPingMs = (message.resource?.message?.createdTimestamp ?? Number.NaN) - interaction.createdTimestamp;
		const gatewayPingMs = this.container.client.ws.ping;

		const roundTripPing = roundTripPingMs > 0 ? `${roundTripPingMs}ms` : "(an error occurred)";
		const gatewayPing = gatewayPingMs > 0 ? `${gatewayPingMs}ms` : "(not enough gateway events have happened yet)";

		return interaction.editReply({
			content: `Gateway ping: ${gatewayPing}.\nEffective ping: ${roundTripPing}.`,
		});
	}
}
