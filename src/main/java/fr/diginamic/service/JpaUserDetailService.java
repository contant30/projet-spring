package fr.diginamic.service;

import fr.diginamic.entites.Role;
import fr.diginamic.entites.Utilisateur;
import fr.diginamic.repository.UtilisateurRepository;
import jakarta.annotation.PostConstruct;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service("userDetailsService")
public class JpaUserDetailService implements UserDetailsService {

     @Autowired
    private PasswordEncoder encoder;

     @Autowired
    private UtilisateurRepository utilisateurRepository;

     @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException{
         return utilisateurRepository.findByUsername(username);
     }

    @Transactional
    @PostConstruct
    public void chargerUtilisateur(){

         if (utilisateurRepository.findAll().isEmpty()){

             Utilisateur admin = new Utilisateur("admin", encoder.encode("admin"), new Role("ROLE_ADMIN"));
             utilisateurRepository.save(admin);

             Utilisateur user = new Utilisateur("user", encoder.encode("user"), new Role("ROLE_USER"));
             utilisateurRepository.save(user);
         }
     }
}
