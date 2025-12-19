package fr.diginamic.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class VilleException {


    @ExceptionHandler({VilleApiException.class})
    protected ResponseEntity<String> afficherErreurs(VilleApiException ex){

        return  ResponseEntity.badRequest().body(ex.getMessage());
    }

}
