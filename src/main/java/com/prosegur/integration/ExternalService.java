package com.prosegur.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import com.squareup.okhttp.ResponseBody;
import org.apache.commons.lang3.tuple.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.SocketTimeoutException;

/**
 * Service example
 */
@Service
public class ExternalService {

    private static final Logger logger = LoggerFactory.getLogger(ExternalService.class);
    private final ObjectMapper objectMapper = new ObjectMapper();

    /**
     * Contructor method
     */
    public ExternalService() {}

    /**
     * Method for send request to AEMET API
     * @param url request url
     * @param apiKey AEMET API KEY
     * @return AEMET response
     */
    public Pair<Integer, Object> sendRequest(String url, String apiKey) {

        int code = 0;
        String jsonString = "";

        String finalUrl = url + "?api_key=" + apiKey;

        try {
            Response response = executeRequest(finalUrl);

            ResponseBody responseBody = response.body();
            code = response.code();
            Object entity = objectMapper.readValue(responseBody.string(), Object.class);

            if (entity != null) {
                jsonString = entity.toString();
            }
        } catch (SocketTimeoutException e) {
            logger.error("Http ERROR: Tiempo de espera agotado: {}", e.getMessage(), e);
            code = 408;
            jsonString = e.getMessage();
        } catch (IOException e) {
            logger.error("Http ERROR: Fallo al crear el cliente http: {}", e.getMessage(), e);
            code = 400;
            jsonString = e.getMessage();
        } finally {
            logger.trace("Code: [{}]", code);
            logger.trace("Result: [{}]", jsonString);
        }
        return Pair.of(code, jsonString);
    }

    private Response executeRequest(String url) throws IOException {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(url)
                .get()
                .addHeader("cache-control", "no-cache")
                .build();
        return client.newCall(request).execute();
    }

}
