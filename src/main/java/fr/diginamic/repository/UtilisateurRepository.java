package fr.diginamic.repository;

import fr.diginamic.entites.Utilisateur;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UtilisateurRepository extends JpaRepository<Utilisateur, Long> {

    // Recherche un utilisateur par son nom
     Utilisateur findByUsername(String nom);

}
