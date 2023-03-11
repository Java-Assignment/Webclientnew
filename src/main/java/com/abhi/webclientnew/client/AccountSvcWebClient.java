package com.abhi.webclientnew.client;

import com.abhi.webclientnew.dto.AccountDTO;
import com.abhi.webclientnew.dto.AddAccountDTO;
import com.abhi.webclientnew.dto.UpdateAccountDTO;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.abhi.webclientnew.config.AppProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Mono;

import java.net.URI;

@Component
@Slf4j
public class AccountSvcWebClient implements AccountSvcClient{

    private AppProperties appProperties;

    private WebClient webClient;

    public AccountSvcWebClient(AppProperties appProperties) {
        this.appProperties = appProperties;

        webClient = WebClient.builder()
                .baseUrl(appProperties.getAcsvc())
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .build();

    }


    @Override
    public AccountDTO add(AddAccountDTO addAccountDTO) throws JsonProcessingException {

        Mono<AccountDTO> mono = webClient.post()
                .bodyValue(new ObjectMapper().writeValueAsString(addAccountDTO))
                .exchangeToMono(
                        response -> {
                            if (response.statusCode().is2xxSuccessful()) {
                                return response.bodyToMono(AccountDTO.class);
                            } else {
                                return response.createException().flatMap(Mono::error);
                            }
                        }
                );

        AccountDTO accountDTO = mono.block();
        log.info("AccountSvcWebClient. NEW AC :"+accountDTO);
        return accountDTO;
    }
    @Override
    public AccountDTO get(String accountId) {
        Mono<AccountDTO> mono = webClient.get()
                .uri(accountId)
                .exchangeToMono(
                        response -> {
                            if (response.statusCode().is2xxSuccessful()) {
                                return response.bodyToMono(AccountDTO.class);
                            } else if (response.statusCode().equals(HttpStatus.NOT_FOUND)) {
                                return Mono.empty();
                            } else {
                                return response.createException().flatMap(Mono::error);
                            }
                        }
                );
        AccountDTO accountDTO = mono.block();
        log.info("AccountSvcWebClient GET ac:"+accountDTO);
        return accountDTO;
    }

    @Override
    public AccountDTO update(String accountId, UpdateAccountDTO updateAccountDTO) throws JsonProcessingException {
        AccountDTO accountDTO = webClient.put()
                .uri(accountId)
                .bodyValue(new ObjectMapper().writeValueAsString(updateAccountDTO))
                .exchangeToMono(
                        response -> {
                            if (response.statusCode().is2xxSuccessful()) {
                                return response.bodyToMono(AccountDTO.class);
                            } else {
                                return response.createException().flatMap(Mono::error);
                            }
                        }
                )
                .block();
        log.info("AccountSvcWebClient UPDATE ac:"+accountDTO);
        return accountDTO;
    }

}
