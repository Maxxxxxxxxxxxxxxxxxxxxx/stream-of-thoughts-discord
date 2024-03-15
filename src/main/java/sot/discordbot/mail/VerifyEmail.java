package sot.discordbot.mail;

import java.util.regex.Pattern;

public class VerifyEmail {

    private static final Pattern EMAIL_PATTERN =
            Pattern.compile("^[a-zA-Z0-9._%+-]+@(?:ug.edu.pl|studms.ug.edu.pl|gumed.edu.pl)$");

    public static boolean verify(String emailString) {
        return EMAIL_PATTERN.matcher(emailString).matches();
    }
}
