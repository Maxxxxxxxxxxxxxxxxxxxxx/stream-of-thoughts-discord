package sot.discordbot.verification;

import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class TokenService {

    private List<Token> tokens;

    public TokenService() { this.tokens = new ArrayList<>(); }

    public String generateToken() {
        Random random = new Random();
        String tokenString = Arrays.stream(new int[4])
                .map(element -> random.nextInt(10))
                .mapToObj(String::valueOf)
                .collect(Collectors.joining());

        tokens.add(new Token(2137, tokenString));
        return tokenString;
    }

    public boolean verifyToken(String token) {
        return tokens.stream().anyMatch(t -> t.content().equals(token));
    }
}
