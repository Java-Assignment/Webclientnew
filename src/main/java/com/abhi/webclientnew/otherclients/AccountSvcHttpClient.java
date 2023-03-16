package com.abhi.webclientnew.otherclients;

import com.abhi.webclientnew.config.AppProperties;
import com.abhi.webclientnew.dto.AccountDTO;
import com.abhi.webclientnew.dto.AddAccountDTO;
import com.abhi.webclientnew.exception.AppException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.lang.reflect.Type;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;

@Component("httpclient")
@Slf4j
public class AccountSvcHttpClient implements AccountSvcOtherClient {
    @Autowired
    private AppProperties appProperties;

    public AccountSvcHttpClient(AppProperties appProperties) {
        this.appProperties = appProperties;
    }

    private HttpClient httpClient = HttpClient.newHttpClient();

    @Override
    public AccountDTO add(AddAccountDTO addAccountDTO) throws IOException, AppException, InterruptedException {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        String acJson = objectMapper.writeValueAsString(addAccountDTO);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(appProperties.getAcsvc()))
                .POST(HttpRequest.BodyPublishers.ofString(acJson))
                .headers(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .build();
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        if (response.statusCode() == 201) {
            log.info("AccountSvcHttpClient ADD Ac:" + response.body());
            AccountDTO accountDTO = objectMapper.readValue(response.body(), AccountDTO.class);
            return accountDTO;
        } else {
            throw new AppException("Add account failed.");
        }

    }

    @Override
    public AccountDTO get(String accountId) throws AppException, IOException, InterruptedException {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(appProperties.getAcsvc() + "/" + accountId))
                .GET()
                .headers(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .build();
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        if (response.statusCode() == 200) {
            log.info("AccountSvcHttpClient GET Ac:" + response.body());
            AccountDTO accountDTO = objectMapper.readValue(response.body(), AccountDTO.class);
            return accountDTO;
        } else if (response.statusCode() == 404) {
            return null;
        } else {
            throw new AppException("Get account failed");
        }

    }

    @Override
    public List<AccountDTO> getAccounts() throws IOException, InterruptedException, AppException {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(appProperties.getAcsvc()))
                .GET()
                .headers(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .build();
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        if (response.statusCode() == 200) {
            log.info("AccountSvcHttpClient GET Ac:" + response.body());
            List<AccountDTO> accountDTOList = objectMapper.readValue(response.body(), new TypeReference<>() {
            });
            return accountDTOList;
        } else {
            throw new AppException("Get Accounts failed");
        }


    }
}
