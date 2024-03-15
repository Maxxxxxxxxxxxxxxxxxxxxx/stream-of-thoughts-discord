package sot.discordbot.mail;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import sot.discordbot.discord.Log;

@Service
public class MailingService {

    private final JavaMailSender javaMailSender;

    public MailingService(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }

    public void sendOneTimeTokenEmail(String to, String token) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject("Verification token");
        message.setText(String.format("Your verification token is %s", token));

        Log.logger.debug("Sent verification email to {}", to);
        javaMailSender.send(message);
    }
}
