package com.banbta.bbogcatrlaopenaibacktesting;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@SpringBootApplication
public class BbogCatRlaOpenaiBackTestingApplication {

    public static void main(String[] args) {
        Dotenv dotenv = Dotenv.load();
        System.setProperty("AZURE_OPENAI_APIKEY", dotenv.get("AZURE_OPENAI_APIKEY"));
        System.setProperty("AZURE_OPENAI_ENDPOINT", dotenv.get("AZURE_OPENAI_ENDPOINT"));
        SpringApplication.run(BbogCatRlaOpenaiBackTestingApplication.class, args);
    }


}
