package fr.diginamic.mapper;

import fr.diginamic.DTO.VilleDto;
import fr.diginamic.entites.Ville;

public interface IVilleMapper {
    VilleDto toDto(Ville ville);

    Ville toBean(VilleDto dto);
}
