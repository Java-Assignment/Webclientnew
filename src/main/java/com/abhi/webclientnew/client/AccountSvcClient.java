package com.abhi.webclientnew.client;

import com.abhi.webclientnew.dto.UpdateAccountDTO;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.abhi.webclientnew.dto.AccountDTO;
import com.abhi.webclientnew.dto.AddAccountDTO;

import java.util.List;

public interface AccountSvcClient {
    AccountDTO add(AddAccountDTO addAccountDTO) throws JsonProcessingException;
    AccountDTO get(String accountId);
    AccountDTO update(String accountId,UpdateAccountDTO updateAccountDTO) throws JsonProcessingException;
    AccountDTO addNotes(String accountId, List<String> notes) throws JsonProcessingException;
    void delete(String accountId);
    List<AccountDTO> getAccounts();
}
