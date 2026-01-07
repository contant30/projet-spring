package fr.diginamic.controleurs;

import fr.diginamic.DTO.DepartementDto;
import fr.diginamic.DTO.VilleDto;
import fr.diginamic.exception.VilleApiException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
import java.util.Optional;

public interface IVilleControleur {
    @GetMapping
    List<VilleDto> getVille();

    @Operation(summary = "Ajoute une ville")
    @ApiResponses(value={
            @ApiResponse(responseCode = "200",
                    description = "Ville au format JSON",
                    content={@Content(mediaType = "application/json",
                            schema = @Schema(implementation = VilleDto.class)),
                    }),
            @ApiResponse(responseCode = "400",
                    description = "Ville non ajoutée", content = @Content())
    })
    ResponseEntity<String> ajouterVille(@Valid @RequestBody VilleDto villeDto, BindingResult result);

    @PutMapping("/{id}")
    ResponseEntity<String> modifierVille(@PathVariable int id, @Valid @RequestBody VilleDto villeDto, BindingResult result) throws VilleApiException;

    @GetMapping("/{id}")
    Optional<VilleDto> getVilleId(@PathVariable int id);


    @Operation(summary = "supprime une ville par son id")
    @ApiResponses(value={
            @ApiResponse(responseCode = "200",
                    description = "Ville au format JSON",
                    content={@Content(mediaType = "application/json",
                            schema = @Schema(implementation = VilleDto.class)),
                    }),
            @ApiResponse(responseCode = "400",
                    description = "Ville non supprimé", content = @Content())
    })
    @DeleteMapping("/{id}")
    ResponseEntity<String> supprimerVille(@PathVariable int id);

    @Operation(summary = "Retourne une ville et sa population à partir de son nom")
    @ApiResponses(value={
            @ApiResponse(responseCode = "200",
                    description = "Ville au format JSON",
                    content={@Content(mediaType = "application/json",
                            schema = @Schema(implementation = VilleDto.class)),
                    }),
            @ApiResponse(responseCode = "400",
                    description = "Ville non trouvé", content = @Content())
    })
    VilleDto getVillePopulationNom(@PathVariable String nom) throws VilleApiException;

    @Operation(summary = "Retourne les villes dont la population est supérieure à min")
    @ApiResponses(value={
            @ApiResponse(responseCode = "200",
                    description = "Ville au format JSON",
                    content={@Content(mediaType = "application/json",
                            array = @ArraySchema(schema =
                            @Schema(implementation = VilleDto.class))),
                    }),
            @ApiResponse(responseCode = "400",
                    description = "Ville non trouvé", content = @Content())
    })
    List<VilleDto> getVillePopulation(@PathVariable Integer min);


    @Operation(summary = "Retourne les villes dont la population est supérieure à min et inferrieur a sup")
    @ApiResponses(value={
            @ApiResponse(responseCode = "200",
                    description = "Ville au format JSON",
                    content={@Content(mediaType = "application/json",
                            schema = @Schema(implementation = VilleDto.class)),
                    }),
            @ApiResponse(responseCode = "400",
                    description = "Ville non trouvé", content = @Content())
    })
    List<VilleDto> getVillePopulation(@Parameter(description = "valeur minimum de la population",example = "32569",required = true)Integer min,
                                      @Parameter(description = "valeur maximum de la population",example = "12563652",required = true)Integer max)
            throws VilleApiException;

    List<VilleDto> getVillesSupMinDep(@PathVariable String code, @PathVariable Integer min);

    List<VilleDto> getVillesSupMinInfMaxDep(@PathVariable String code, @PathVariable Integer min, @PathVariable Integer max);

    List<VilleDto> getTopVillesDepartement(@PathVariable String code, @PathVariable int n);

    List<VilleDto> getVilleChaine(@PathVariable String chaine) throws VilleApiException;
}
