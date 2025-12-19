package fr.diginamic.hello.mapper;

import fr.diginamic.hello.DTO.VilleDto;
import fr.diginamic.hello.entites.Ville;
import org.springframework.stereotype.Component;

@Component
public class VilleMapper {

    public VilleDto toDto (Ville ville){
        VilleDto dto = new VilleDto();
        dto.setNom(ville.getNom());
        dto.setPopulation(ville.getPopulation());

        return dto;

    }

    public Ville toBean(VilleDto dto) {
        Ville ville = new Ville();
        ville.setNom(dto.getNom());
        ville.setPopulation(dto.getPopulation());
        return ville;
    }


}
