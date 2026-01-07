package fr.diginamic.repository;

import fr.diginamic.entites.Departement;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface DepartementRepository extends CrudRepository<Departement, Long> {


    List<Departement> findAll();

   Departement findById(long id);

    Departement findByCodePostale(String code);

    Departement findByNomIgnoreCase(String nom);











}
