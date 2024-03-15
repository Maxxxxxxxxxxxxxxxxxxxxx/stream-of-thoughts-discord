package sot.discordbot.discord;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties("discord")
public class BotProperties {
    private String token;
}
