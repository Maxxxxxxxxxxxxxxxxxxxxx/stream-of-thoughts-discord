package sot.discordbot.discord.command.impl;

import discord4j.common.util.Snowflake;
import discord4j.core.event.domain.interaction.ChatInputInteractionEvent;
import discord4j.core.object.command.ApplicationCommandOption;
import discord4j.core.object.entity.Guild;
import discord4j.core.object.entity.Role;
import discord4j.discordjson.json.ApplicationCommandOptionData;
import discord4j.discordjson.json.ApplicationCommandRequest;
import discord4j.discordjson.json.RoleData;
import reactor.core.publisher.Mono;
import sot.discordbot.discord.Bot;
import sot.discordbot.discord.Log;
import sot.discordbot.discord.command.ISlashCommand;

public class VerifySlashCommand implements ISlashCommand {

    @Override
    public String getCommandName() {
        return "verify";
    }

    @Override
    public ApplicationCommandRequest buildCommand() {
        return ApplicationCommandRequest.builder()
                .name(getCommandName())
                .description("Zweryfikuj email")
                .addOption(ApplicationCommandOptionData.builder()
                        .name("kod")
                        .description("Podaj 4-cyfrowy kod wysłany na email")
                        .type(ApplicationCommandOption.Type.STRING.getValue())
                        .required(true)
                        .maxLength(4)
                        .minLength(4)
                        .build()
                ).build();
    }

    @Override
    public Mono<Void> on(Bot bot, ChatInputInteractionEvent event) {
        String tokenValue = event.getOption("kod").get().getValue().get().asString();

        if (bot.getTokenService().verifyToken(tokenValue)) {
            RoleData roleData = event.getInteraction().getGuild()
                    .flatMapMany(Guild::getRoles)
                    .map(Role::getData)
                    .filter(role -> role.name().equals("Zweryfikowany"))
                    .blockFirst();

            event.getInteraction().getMember().get().addRole(Snowflake.of(roleData.id())).subscribe();

            Log.logger.debug("User verify success");
            return event.reply("Zweryfikowano!").withEphemeral(true);
        } else {
            Log.logger.debug("User verify failure");
            return event.reply("Kod nieprawidłowy. Spróbuj ponownie za pomocą /login.").withEphemeral(true);
        }
    }
}
