package fr.diginamic.hello.service;

import org.springframework.stereotype.Service;

@Service
public class HelloService {

    public static String salutation(){
        return"je suis la classe de service et je vous dit bonjour";
    }
}
