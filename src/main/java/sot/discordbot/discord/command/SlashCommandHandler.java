package sot.discordbot.discord.command;

import discord4j.core.event.domain.interaction.ChatInputInteractionEvent;
import discord4j.rest.service.ApplicationService;
import sot.discordbot.discord.Bot;
import sot.discordbot.discord.Log;
import sot.discordbot.discord.command.impl.LoginSlashCommand;
import sot.discordbot.discord.command.impl.VerifySlashCommand;

import java.util.HashMap;

public class SlashCommandHandler {

    public static HashMap<String, ISlashCommand> commands = new HashMap<>();

    private static void setCommand(ISlashCommand command) {
        commands.put(command.getCommandName(), command);
    }

    static {
        setCommand(new LoginSlashCommand());
        setCommand(new VerifySlashCommand());
    }

    public SlashCommandHandler(Bot bot) {
        ApplicationService service = bot.getClient().getRestClient().getApplicationService();
        Long appId = bot.getApplicationId();

        service.getGlobalApplicationCommands(appId)
                .filter(data -> !commands.containsKey(data.name()))
                .flatMap(data -> service.deleteGlobalApplicationCommand(appId, data.id().asLong()))
                .subscribe();

        bot.getClient().on(ChatInputInteractionEvent.class, event -> {
            String commandName = event.getCommandName();

            if (!commands.containsKey(commandName)) {
                Log.logger.warn("Called unknown command \"{}\"", commandName);
                return event.reply("Unknown command").withEphemeral(true);
            }

            ISlashCommand command = commands.get(commandName);
            return command.on(bot, event);
        }).subscribe();

        commands.values().forEach(command -> {
            Log.logger.info("Initialized slash command: /{}", command.getCommandName());
            service.createGlobalApplicationCommand(appId, command.buildCommand()).subscribe();
        });
    }
}
