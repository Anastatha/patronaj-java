package org.example.pat.infrastructure;

import org.example.pat.exception.ForbiddenError;
import org.example.pat.exception.NotFoundInnError;
import org.example.pat.exception.NotFoundOkvedError;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.Map;

@Service
public class DadataService {

    @Value("${dadata.token}")
    private String token;

    @Value("${dadata.url}")
    private String dadataUrl;

    @Value("${dadata.url.okved}")
    private String dadataOkvedUrl;

    private final RestClient restClient;

    public DadataService(RestClient.Builder restClientBuilder) {
        this.restClient = restClientBuilder.build();
    }

    public String getOkved(String inn) {
        try {
            Map<String, Object> responseBody = restClient.post()
                    .uri(dadataUrl)
                    .contentType(MediaType.APPLICATION_JSON)
                    .accept(MediaType.APPLICATION_JSON)
                    .header("Authorization", "Token " + token)
                    .body(Map.of("query", inn))
                    .retrieve()
                    .onStatus(status -> status.value() == 403, (request, response) -> {
                        throw new ForbiddenError("Invalid or missing token.");
                    })
                    .body(Map.class);

            if (responseBody != null && responseBody.containsKey("suggestions")) {
                java.util.List<Map<String, Object>> suggestions =
                        (java.util.List<Map<String, Object>>) responseBody.get("suggestions");
                if (suggestions != null && !suggestions.isEmpty()) {
                    Map<String, Object> data = (Map<String, Object>) suggestions.get(0).get("data");
                    return (String) data.get("okved");
                }
            }
            throw new NotFoundInnError();

        } catch (ForbiddenError | NotFoundInnError e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("Ошибка при получении ОКВЭД", e);
        }
    }

    public String getOkvedName(String okved) {
        try {
            Map<String, Object> response = restClient.post()
                    .uri(dadataOkvedUrl)
                    .contentType(MediaType.APPLICATION_JSON)
                    .accept(MediaType.APPLICATION_JSON)
                    .header("Authorization", "Token " + token)
                    .body(Map.of("query", okved))
                    .retrieve()
                    .body(Map.class);

            if (response != null && response.containsKey("suggestions")) {
                java.util.List<Map<String, Object>> suggestions =
                        (java.util.List<Map<String, Object>>) response.get("suggestions");
                if (suggestions != null && !suggestions.isEmpty()) {
                    return (String) suggestions.get(0).get("value");
                }
            }
            throw new NotFoundOkvedError();

        } catch (Exception e) {
            throw new NotFoundOkvedError();
        }
    }
}