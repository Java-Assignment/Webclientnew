package com.abhi.webclientnew;

import com.abhi.webclientnew.client.AccountSvcClient;
import com.abhi.webclientnew.dto.AccountDTO;
import com.abhi.webclientnew.dto.AccountStatus;
import com.abhi.webclientnew.dto.AccountType;
import com.abhi.webclientnew.dto.AddAccountDTO;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.containing;

@SpringBootTest(properties = {"com.abhi.acsvc=http://localhost:9090/accounts/"})
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@Slf4j
public class TestUsingWireMock {

    private WireMockServer wireMockServer;

    @Autowired
    private AccountSvcClient accountSvcClient;

    private AddAccountDTO newAccount;
    @BeforeAll
    public void setup() throws JsonProcessingException {
        List<String> notes = new ArrayList<>();
        notes.add("notes for demo");

        newAccount = new AddAccountDTO(AccountType.SAVING, "Cust 1", notes, AccountStatus.ACTIVE, "NA");
        AccountDTO testAccountDTO = new AccountDTO("0123456789", AccountType.SAVING, "Cust 1", notes, AccountStatus.ACTIVE, "NA", LocalDate.now());

        // mock server
        wireMockServer = new WireMockServer(9090);
        wireMockServer.start();

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());

        //stubs
        // ADD
        wireMockServer.stubFor(WireMock.post(WireMock.urlMatching("/accounts/"))
                .withRequestBody(containing("Cust"))
                .willReturn(aResponse().withHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                        .withStatus(201)
                        .withBody(objectMapper.writeValueAsString(testAccountDTO))));

        // GET
        wireMockServer.stubFor(WireMock.get(WireMock.urlMatching("/accounts/0123456789"))
                .willReturn(aResponse().withHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                        .withStatus(200)
                        .withBody(objectMapper.writeValueAsString(testAccountDTO))));



    }

    @Test
    public void testAdd() throws JsonProcessingException {
        AccountDTO add = accountSvcClient.add(newAccount);
        Assertions.assertEquals("0123456789", add.getAccountId());
    }

    @Test
    public void testGet() throws JsonProcessingException {
        AccountDTO add = accountSvcClient.get("0123456789");
        Assertions.assertEquals("0123456789", add.getAccountId());
    }


}
