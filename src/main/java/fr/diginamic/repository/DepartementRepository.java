package fr.diginamic.repository;

import fr.diginamic.entites.Departement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface DepartementRepository extends JpaRepository<Departement, Long> {


    List<Departement> findAll();

   Departement findById(long id);

   //Departement findByCode(String codePostale);

    Optional<Departement> findByNomIgnoreCase(String nom);

    Optional<Departement> findByCode(String code);











}
