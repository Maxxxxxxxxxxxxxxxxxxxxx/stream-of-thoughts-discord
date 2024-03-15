package ugdiscord.app.discord;

import discord4j.common.util.Snowflake;
import discord4j.core.DiscordClient;
import discord4j.core.GatewayDiscordClient;
import discord4j.core.event.domain.lifecycle.ReadyEvent;
import discord4j.core.object.entity.User;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import reactor.core.publisher.Mono;
import ugdiscord.app.discord.command.SlashCommandHandler;
import ugdiscord.app.mail.MailingService;
import ugdiscord.app.verification.TokenService;

@Component
public class Bot {

    @Getter private Long testGuildId;
    @Getter private final GatewayDiscordClient client;
    @Getter private final Long applicationId;
    @Getter private final MailingService mailingService;
    @Getter private final TokenService tokenService;

    private final SlashCommandHandler slashCommandHandler;

    public Bot(BotProperties properties, MailingService mailingService, TokenService tokenService, @Value("${testGuildId}") Long guild) {
        DiscordClient discordClient = DiscordClient.create(properties.getToken());

        this.testGuildId = guild;
        this.tokenService = tokenService;
        this.mailingService = mailingService;
        this.client = discordClient.gateway().login().block();
        this.applicationId = client.getRestClient().getApplicationId().block();
        this.slashCommandHandler = new SlashCommandHandler(this);

        client.on(ReadyEvent.class, event -> Mono.fromRunnable(() -> {
            final User self = event.getSelf();
            Log.logger.info("Discord: logged in as {}", self.getUsername());
        })).subscribe();
    }
}
