package fr.diginamic.controleurs;

import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import fr.diginamic.DTO.DepartementDto;
import fr.diginamic.DTO.VilleDto;
import fr.diginamic.entites.Departement;
import fr.diginamic.entites.Ville;
import fr.diginamic.exception.VilleApiException;
import fr.diginamic.mapper.DepartementMapper;
import fr.diginamic.service.IVilleService;
import fr.diginamic.service.iDepartementService;
import jakarta.servlet.http.HttpServletResponse;
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

import java.awt.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/departement")
public class DepartementControleur {

    @Autowired
    private iDepartementService iDepartementService;

    @Autowired
    private DepartementMapper departementMapper;

    @Autowired
    private IVilleService IVilleService;




    /**
     * Constructeur
     * @param iDepartementService
     */
    public DepartementControleur(iDepartementService iDepartementService) {
        this.iDepartementService = iDepartementService;
    }

    /**
     * @return une liste de departement
     */
    @GetMapping
    public List<DepartementDto> getDepartement() {
        List<Departement> departements = iDepartementService.extraireDepartement();
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

            List<Departement> departements = Collections.singletonList(iDepartementService.ajouterDepartement(departement));
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

        iDepartementService.modifierDepartementNom(id, departement);
        return ResponseEntity.ok("La ville " +departementDto.getNom() + " a été modifiée");
    }

    @GetMapping("/code/{code}")
    public ResponseEntity<DepartementDto> rechercherDepartementCode(@PathVariable String code) {
        System.out.println("Recherche département code = " + code);

        Departement departement = iDepartementService.extractDepartementCode(code);

        if (departement == null) {
            return ResponseEntity.notFound().build();
        }

        DepartementDto dto = departementMapper.toDto(departement); // Mapper vers DTO
        return ResponseEntity.ok(dto);
    }

    /**
     * fichier PDF affiche les villes par rapport à un département
     * @param code
     * @param response
     * @throws Exception
     */
    @GetMapping("/code/{code}/fiche")
    public void ficheDepartement(@PathVariable String code, HttpServletResponse response) throws Exception {

        response.setContentType("application/pdf");
        response.setHeader("Content-Disposition", "attachment; filename=\"fichier.pdf\"");


        Departement departement = iDepartementService.extractDepartementCode(code);

        Document document = new Document(PageSize.A4);
        PdfWriter.getInstance(document, response.getOutputStream());

        document.open();

        // Titre
        document.addTitle("Fiche");

        // Code département
        Font infoFont = new Font(Font.FontFamily.HELVETICA, 14, Font.BOLD);
        Paragraph codePara = new Paragraph("Code département recherché : " + code, infoFont);
        codePara.setAlignment(Element.ALIGN_CENTER);
        codePara.setSpacingAfter(30f);
        document.add(codePara);

        // Date du jour
        LocalDateTime maintenant = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        String dateStr = maintenant.format(formatter);

        Font dateFont = new Font(Font.FontFamily.HELVETICA, 10, Font.ITALIC);
        Paragraph datePara = new Paragraph("Généré le : " + dateStr, dateFont);
        datePara.setAlignment(Element.ALIGN_CENTER);
        codePara.setSpacingAfter(30f);
        document.add(datePara);

        // Ligne vide
        document.add(new Paragraph(" "));


        // tableau des villes
        PdfPTable table = new PdfPTable(2);
        table.setWidthPercentage(100);
        table.setWidths(new float[]{70f, 30f});

        Font headerFont = new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD);
        PdfPCell headerVille = new PdfPCell(new Paragraph("Ville", headerFont));
        headerVille.setHorizontalAlignment(Element.ALIGN_CENTER);
        headerVille.setPadding(10f);
        table.addCell(headerVille);

        PdfPCell headerPop = new PdfPCell(new Paragraph("Population", headerFont));
        headerPop.setHorizontalAlignment(Element.ALIGN_CENTER);
        headerPop.setPadding(10f);
        table.addCell(headerPop);

        // Villes recherchés
        List<Ville> villes = IVilleService.extraireVilleParDepartementCode(code); // ou getListeVilles()

        if (villes != null && !villes.isEmpty()) {
            for (Ville ville : villes) {
                table.addCell(ville.getNom());
                table.addCell(String.valueOf(ville.getPopulation()));
            }
        }

        document.add(table);
        document.close();
    }
    }

