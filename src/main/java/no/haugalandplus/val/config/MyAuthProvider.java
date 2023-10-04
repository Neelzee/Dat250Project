package no.haugalandplus.val.config;

import no.haugalandplus.val.entities.User;
import no.haugalandplus.val.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

import java.util.ArrayList;

@Component
public class MyAuthProvider implements AuthenticationProvider {

    @Autowired
    private UserRepository userRepository;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String providedUsername = authentication.getPrincipal().toString();
        User user = userRepository.getUserByUsername(providedUsername);

        String providedPassword = authentication.getCredentials().toString();
        String correctPassword = user.getPassword();

        // Authenticate
        // If Passwords don't match throw and exception
        if (!providedPassword.equals(correctPassword))
            throw new RuntimeException("Incorrect Credentials");

        // return Authentication Object
        Authentication authenticationResult =
                new UsernamePasswordAuthenticationToken(user, authentication.getCredentials(), new ArrayList<>());
        return authenticationResult;
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }
}