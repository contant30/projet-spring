package fr.diginamic.securite;

import fr.diginamic.entites.Role;
import fr.diginamic.entites.Utilisateur;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class MemoireService implements UserDetailsService {

    @Autowired
    private PasswordEncoder encoder;


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException{

        if (username.equals("ADMIN")){
            return new Utilisateur(username, encoder.encode("admin"), new Role("ROLE_ADMIN"));
        }
        else if (username.equals("USER")) {
            return new Utilisateur(username, encoder.encode("user"), new Role("ROLE_USER"));
        }
        else {
            throw new UsernameNotFoundException("utilisateur " + username + " inconnu.");
        }
    }

}
