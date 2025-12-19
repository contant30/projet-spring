package fr.diginamic.controleurs;

import fr.diginamic.DTO.DepartementDto;
import fr.diginamic.entites.Departement;
import fr.diginamic.exception.VilleApiException;
import fr.diginamic.mapper.DepartementMapper;
import fr.diginamic.service.DepartementService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/departement")
public class DepartementControleur {

    @Autowired
    private final DepartementService departementService;

    @Autowired
    private DepartementMapper departementMapper;

    /**
     * Constructeur
     * @param departementService
     */
    public DepartementControleur(DepartementService departementService) {
        this.departementService = departementService;
    }

    /**
     * @return une liste de departement
     */
    @GetMapping
    public List<DepartementDto> getDepartement() {
        List<Departement> departements = departementService.extraireDepartement();
        return departements.stream().map(departementMapper::toDto).collect(Collectors.toList());
    }

    /**
     * @param departementDto le nom du departement à ajouter
     * @param result
     * @return une liste de departement
     */
    @PostMapping
    public ResponseEntity<String> ajouterDepartement(@Valid @RequestBody DepartementDto departementDto, BindingResult result) {
        try {
            if (result.hasErrors()) {
                List<FieldError> errors = result.getFieldErrors();
                String message = errors.stream().map(e -> e.getField() + " : " + e.getDefaultMessage()).collect(Collectors.joining(", "));
                return ResponseEntity.badRequest().body(message);
            }

            Departement departement = departementMapper.toBean(departementDto);

            List<Departement> departements = departementService.ajouterDepartement(departement);
            String noms = departements.stream().map(Departement::getNom).collect(Collectors.joining(", "));

            return ResponseEntity.ok("Les départements suivantes sont maintenant en base : " + noms);

        } catch (VilleApiException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    /**
     * Modifie un département par son id
     * @param id
     * @param departementDto
     * @param result
     * @return
     * @throws VilleApiException
     */
    @PutMapping("/{id}")
    public ResponseEntity<String> modifierDepartement(@PathVariable Long id, @Valid @RequestBody DepartementDto departementDto, BindingResult result) throws VilleApiException {
        System.out.println("Modification du département id = " + id);

        if (result.hasErrors()) {
            List<FieldError> errors = result.getFieldErrors();
            String message = errors.stream().map(e -> e.getField() + " : " + e.getDefaultMessage()).collect(Collectors.joining(", "));
            throw new VilleApiException(message);
        }

        Departement departement = departementMapper.toBean(departementDto);

        departementService.modifierDepartementNom(id, departement);
        return ResponseEntity.ok("La ville " +departementDto.getNom() + " a été modifiée");
    }
}
