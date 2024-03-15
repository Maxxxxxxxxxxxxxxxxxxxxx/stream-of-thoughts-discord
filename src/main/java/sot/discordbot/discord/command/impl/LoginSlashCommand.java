package sot.discordbot.discord.command.impl;

import discord4j.core.event.domain.interaction.ChatInputInteractionEvent;
import discord4j.core.object.command.ApplicationCommandOption;
import discord4j.discordjson.json.ApplicationCommandOptionData;
import discord4j.discordjson.json.ApplicationCommandRequest;
import reactor.core.publisher.Mono;
import sot.discordbot.discord.Bot;
import sot.discordbot.discord.command.ISlashCommand;
import sot.discordbot.mail.VerifyEmail;

public class LoginSlashCommand implements ISlashCommand {

    @Override
    public String getCommandName() {
        return "login";
    }

    @Override
    public ApplicationCommandRequest buildCommand() {
        return ApplicationCommandRequest.builder()
                .name(getCommandName())
                .description("Zaloguj się za pomocą CAS")
                .addOption(ApplicationCommandOptionData.builder()
                        .name("email")
                        .description("Twój email uczelniany (...gumed.edu.pl / ...ug.edu.pl / ...studms.ug.edu.pl)")
                        .type(ApplicationCommandOption.Type.STRING.getValue())
                        .required(true)
                        .build()
                ).build();
    }

    @Override
    public Mono<Void> on(Bot bot, ChatInputInteractionEvent event) {
        String userEmail = event.getOption("email").get().getValue().get().asString();
        String userToken = bot.getTokenService().generateToken();

        if (VerifyEmail.verify(userEmail)) {
            bot.getMailingService().sendOneTimeTokenEmail(userEmail, userToken);

            return event.reply("Wysłano 4-cyfrowy kod na twój email. Zweryfikuj za pomocą /verify")
                    .withEphemeral(true);
        } else {
            return event.reply("Niepoprawny email. Podaj email uczelniany (...gumed.edu.pl / ...ug.edu.pl / ...studms.ug.edu.pl)")
                    .withEphemeral(true);
        }
    }
}
