package fr.diginamic.mapper;

import fr.diginamic.DTO.DepartementDto;
import fr.diginamic.DTO.VilleDto;
import fr.diginamic.entites.Ville;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class VilleMapper implements IVilleMapper {

    @Autowired
    private DepartementMapper departementMapper;

    @Override
    public VilleDto toDto(Ville ville){
        VilleDto dto = new VilleDto();
        dto.setId(ville.getId());
        dto.setNom(ville.getNom());
        dto.setPopulation(ville.getPopulation());

        if (ville.getDepartement() !=null){
            DepartementDto depDTO= departementMapper.toDto(ville.getDepartement());
            dto.setDepartementDto(depDTO);
            dto.setCodeDepartement(ville.getDepartement().getCode());
            dto.setIdDepartement(ville.getDepartement().getId());
        }
        return dto;
    }

    @Override
    public Ville toBean(VilleDto dto) {
        Ville ville = new Ville();
        ville.setNom(dto.getNom());
        ville.setPopulation(dto.getPopulation());
        ville.setId(dto.getId());

        return ville;
    }


}
