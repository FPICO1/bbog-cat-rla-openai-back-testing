package com.banbta.bbogcatrlaopenaibacktesting.application.services;


import com.azure.ai.openai.OpenAIClient;
import com.azure.ai.openai.OpenAIClientBuilder;
import com.azure.core.credential.AzureKeyCredential;
import com.banbta.bbogcatrlaopenaibacktesting.application.dto.request.DataRequestDTO;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.azure.openai.AzureOpenAiChatModel;
import org.springframework.ai.azure.openai.AzureOpenAiChatOptions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.stringtemplate.v4.ST;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.Scanner;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Service
@Slf4j
public class GenerateReportService {

    private final ConsumeLambdaDownloadS3Service consumeLambdaDownloadS3Service;
    private final AzureOpenAiChatModel azureOpenAiChatModel;
    private final Map<String, Object> cachedReport = new ConcurrentHashMap<>();
    private final Gson gson;
    private final SecretManagerService secretManagerService;


    @Autowired
    public GenerateReportService(
            ConsumeLambdaDownloadS3Service consumeLambdaDownloadS3Service,
            OpenAIClient openAIClient,
            Gson gson,
            SecretManagerService secretManagerService) {

        this.consumeLambdaDownloadS3Service = consumeLambdaDownloadS3Service;
        this.gson = gson;
        this.secretManagerService = secretManagerService;

        // Configurar las opciones de Azure OpenAI
        AzureOpenAiChatOptions options = new AzureOpenAiChatOptions();
        options.setDeploymentName("doc3"); // Configura según tu necesidad
        options.setTemperature(0.7F);

        // Crear el modelo AzureOpenAiChatModel con los objetos configurados
        this.azureOpenAiChatModel = new AzureOpenAiChatModel(openAIClient, options);
    }

    public JsonObject generateReport(DataRequestDTO dataRequestDTO) throws Exception {


        // Aca hay que traer el service de consumo del Lambda.
        JsonObject jsonData = consumeLambdaDownloadS3Service.getDataJson(dataRequestDTO);

        // Cargar el archivo de plantilla
        InputStream templateStream = getClass().getClassLoader().getResourceAsStream("prompts/prompt-report-jmeter.st");
        if (templateStream == null) {
            throw new Exception("El archivo prompt-report-jmeter.st no se encontró en el classpath.");
        }

        String templateContent = "";
        try (Scanner scanner = new Scanner(templateStream, StandardCharsets.UTF_8)) {
            templateContent = scanner.useDelimiter("\\A").next();
        }catch (Exception e){
            System.out.println("no entro al catch creado recientemente");
        }
        System.out.println("templateContent"+templateContent);

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
        JsonObject jsonObject = new JsonObject();

        // Envolver el texto en un JSON válido
        jsonObject.addProperty("reportText", response);

        log.info("Archivo descargado y convertido exitosamente como texto JSON.");
        return jsonObject;

    }
}