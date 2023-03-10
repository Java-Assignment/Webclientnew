package com.abhi.webclientnew;

import com.abhi.webclientnew.client.AccountSvcClient;
import com.abhi.webclientnew.mapper.AccountMapper;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.abhi.webclientnew.dto.*;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;

//import static org.junit.jupiter.api.AssertEquals.assertEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@Slf4j
public class TestWebClient {
    @Autowired
    private AccountSvcClient accountSvcClient;

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
    void addAcTest() throws JsonProcessingException {
        addAc = accountSvcClient.add(addAccountDTO);
        Assertions.assertTrue(addAc.getAccountId() != null);
    }

    @Test
    @DisplayName("Get existing account")
    @Order(2)
    void getAcTest() {
        AccountDTO getAc = accountSvcClient.get(addAc.getAccountId());
        Assertions.assertTrue(getAc.getCustomerName().equals("Cust 2"));
    }

    @Test
    @DisplayName("Get non-existing account")
    @Order(3)
    void getNonExistingAcTest() {
        AccountDTO getAc = accountSvcClient.get("9876543210");
        Assertions.assertNull(getAc);
    }

//    @Test
//    @DisplayName("Update existing account")
//    @Order(4)
//    void updateExistingAcTest() throws JsonProcessingException {
//        UpdateAccountDTO updateAccountDTO = accountMapper.accToUpdAcc(addAc);
//        updateAccountDTO.setAccountStatus(AccountStatus.BLOCKED);
//        AccountDTO updateAc = accountSvcClient.update(addAc.getAccountId(), updateAccountDTO);
//        Assertions.assertAll(
//                () -> assertEquals(AccountStatus.BLOCKED, updateAccountDTO.getAccountStatus()),
//                ()->assertEquals(addAc.getAccountId(),updateAc.getAccountId())
//        );
//    }


}
