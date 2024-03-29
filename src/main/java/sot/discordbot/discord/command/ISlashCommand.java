package sot.discordbot.discord.command;

import discord4j.core.event.domain.interaction.ChatInputInteractionEvent;
import discord4j.discordjson.json.ApplicationCommandRequest;
import reactor.core.publisher.Mono;
import sot.discordbot.discord.Bot;

public interface ISlashCommand {

    String getCommandName();

    default boolean isDefer() {
        return false;
    }

    default boolean isEphemeralWhenDefer() {
        return false;
    }

    ApplicationCommandRequest buildCommand();

    Mono<Void> on(Bot bot, ChatInputInteractionEvent event);
}
