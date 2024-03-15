package ugdiscord.app.discord;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class Log {
    public static Logger logger = LoggerFactory.getLogger(Bot.class);
}
