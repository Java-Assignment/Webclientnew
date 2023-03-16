package com.abhi.webclientnew;

import com.abhi.webclientnew.client.AccountSvcClient;
import com.abhi.webclientnew.dto.*;
import com.abhi.webclientnew.exception.AppException;
import com.abhi.webclientnew.mapper.AccountMapper;
import com.abhi.webclientnew.otherclients.AccountSvcOtherClient;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@Slf4j
public class TestRestTemplate {
    @Autowired
    @Qualifier("resttemplate")
    private AccountSvcOtherClient accountSvcClient;

    @Autowired
    private AccountMapper accountMapper;


    private AddAccountDTO addAccountDTO;
    private AccountDTO addAc;

    @BeforeAll
    public void setup() {
        List<String> notes = new ArrayList<>();
        notes.add("automation test note 1");
        addAccountDTO = new AddAccountDTO(AccountType.SAVING, "Cust 2", notes, AccountStatus.ACTIVE, "NA");
    }

    @Test
    @DisplayName("Add account test")
    @Order(1)
    void addAcTest() throws IOException, AppException, InterruptedException, ExecutionException {
        addAc = accountSvcClient.add(addAccountDTO);
        Assertions.assertTrue(addAc.getAccountId() != null);
    }

    @Test
    @DisplayName("Get existing account")
    @Order(2)
    void getAcTest() throws AppException, IOException, InterruptedException, ExecutionException {
        AccountDTO getAc = accountSvcClient.get(addAc.getAccountId());
        Assertions.assertTrue(getAc.getCustomerName().equals("Cust 2"));
    }

    @Test
    @DisplayName("Get non-existing account")
    @Order(3)
    void getNonExistingAcTest() throws AppException, IOException, InterruptedException, ExecutionException {
        AccountDTO getAc = accountSvcClient.get("9876543210");
        Assertions.assertNull(getAc);
    }

    @Test
    @DisplayName("Get all accounts")
    @Order(4)
    void getAllAccsTest() throws IOException, AppException, InterruptedException, ExecutionException {
        List<String> notes = new ArrayList<>();
        notes.add("Automation test note New");
        AddAccountDTO addAccountDTO1 = new AddAccountDTO(AccountType.CURRENT, "Cust New1", notes, AccountStatus.ACTIVE, "NA");
        AddAccountDTO addAccountDTO2 = new AddAccountDTO(AccountType.CURRENT, "Cust New2", notes, AccountStatus.ACTIVE, "NA");
        accountSvcClient.add(addAccountDTO1);
        accountSvcClient.add(addAccountDTO2);
        List<AccountDTO> accounts = accountSvcClient.getAccounts();
        Assertions.assertAll(
                () -> assertTrue(accounts.size() >= 2),
                () -> assertTrue(accountSvcClient.getAccounts().stream().map(a -> a.getCustomerName()).collect(Collectors.toList()).contains("Cust New1")),
                () -> assertTrue(accountSvcClient.getAccounts().stream().map(a -> a.getCustomerName()).collect(Collectors.toList()).contains("Cust New2"))
        );

    }


}
