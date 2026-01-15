package fr.diginamic.securite;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LoginController {



    @Autowired
    private AuthenticationConfiguration authenticationConfiguration;

    @Autowired
    private JwtUtil jwtUtil;

    @PostMapping("/login")
    public String login(@RequestBody LoginRequest req) {

        // Déclenche la vérification du mot de passe avec UserDetailsService
        authenticationConfiguration.getAuthenticationManager().authenticate(new UsernamePasswordAuthenticationToken(req.getUsername(),
                req.getPassword()));

        // Retourne un JWT
        return jwtUtil.generateToken(req.getUsername());
    }
}





