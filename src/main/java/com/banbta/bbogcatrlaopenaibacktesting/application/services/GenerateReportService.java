package com.banbta.bbogcatrlaopenaibacktesting.application.services;


import com.google.gson.Gson;
import com.google.gson.JsonObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.azure.openai.AzureOpenAiChatModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.stringtemplate.v4.ST;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.Scanner;
import java.util.concurrent.ConcurrentHashMap;

@Service
@Slf4j
public class GenerateReportService {

    private final AzureOpenAiChatModel azureOpenAiChatModel;
    private final Gson gson = new Gson();

    private final Map<String, Object> cachedReport = new ConcurrentHashMap<>();

    @Autowired
    public GenerateReportService(AzureOpenAiChatModel azureOpenAiChatModel) {
        this.azureOpenAiChatModel = azureOpenAiChatModel;
    }

    public Map<String, Object> generateReport() throws Exception {
        // JSON de prueba como variable
        String jsonDataContent = "{\n" +
                "  \"Escenario1\": {\n" +
                "    \"transaction\": \"Total\",\n" +
                "    \"sampleCount\": 8,\n" +
                "    \"errorCount\": 0,\n" +
                "    \"errorPct\": 0,\n" +
                "    \"meanResTime\": 381,\n" +
                "    \"minResTime\": 163,\n" +
                "    \"maxResTime\": 899,\n" +
                "    \"pct1ResTime\": 899,\n" +
                "    \"pct2ResTime\": 899,\n" +
                "    \"pct3ResTime\": 899,\n" +
                "    \"throughput\": 2.3724792408066433,\n" +
                "    \"receivedKBytesPerSec\": 135.63300480056347,\n" +
                "    \"sentKBytesPerSec\": 2.8373021389383157\n" +
                "  },\n" +
                "  \"Escenario2\": {\n" +
                "    \"transaction\": \"Total\",\n" +
                "    \"sampleCount\": 20,\n" +
                "    \"errorCount\": 0,\n" +
                "    \"errorPct\": 0,\n" +
                "    \"meanResTime\": 256.55,\n" +
                "    \"minResTime\": 70,\n" +
                "    \"maxResTime\": 967,\n" +
                "    \"pct1ResTime\": 556.3000000000001,\n" +
                "    \"pct2ResTime\": 946.6999999999997,\n" +
                "    \"pct3ResTime\": 967,\n" +
                "    \"throughput\": 6.3897763578274756,\n" +
                "    \"receivedKBytesPerSec\": 295.2610198682109,\n" +
                "    \"sentKBytesPerSec\": 7.641211561501597\n" +
                "  },\n" +
                "  \"TotalPrueba\": {\n" +
                "    \"totalPeticionesEnviadas\": 1236,\n" +
                "    \"totalPeticionesError\": 160,\n" +
                "    \"errorPct\": 12.94,\n" +
                "    \"meanResTime\": 256.55,\n" +
                "    \"minResTime\": 70,\n" +
                "    \"maxResTime\": 967,\n" +
                "    \"throughput\": 19.3897763578274756\n" +  // Coma eliminada aquí
                "  }\n" +
                "}";



        JsonObject jsonData = gson.fromJson(jsonDataContent, JsonObject.class);

        // Cargar el archivo de plantilla
        InputStream templateStream = getClass().getClassLoader().getResourceAsStream("prompts/prompt-report-jmeter.st");
        if (templateStream == null) {
            throw new Exception("El archivo prompt-report-jmeter.st no se encontró en el classpath.");
        }

        String templateContent;
        try (Scanner scanner = new Scanner(templateStream, StandardCharsets.UTF_8)) {
            templateContent = scanner.useDelimiter("\\A").next();
        }

        ST template = new ST(templateContent, '{', '}');
        template.add("jsonData", jsonData);

        // Generar el prompt final
        String finalPrompt = template.render();

        // Imprimir el prompt antes de enviarlo a Azure OpenAI
        System.out.println("Prompt final antes de enviar a Azure OpenAI:\n" + finalPrompt);

        // Enviar el prompt a Azure OpenAI y recibir la respuesta
        String response = azureOpenAiChatModel.call(finalPrompt);

        // Imprimir la respuesta recibida de Azure OpenAI
        System.out.println("Respuesta de Azure OpenAI:\n" + response);

        // Almacenar el informe generado en el caché y retornarlo
        cachedReport.put("generation", response);
        return Map.of("generation", response);
    }
}