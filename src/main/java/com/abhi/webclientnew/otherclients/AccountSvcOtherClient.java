package com.abhi.webclientnew.otherclients;

import com.abhi.webclientnew.dto.AccountDTO;
import com.abhi.webclientnew.dto.AddAccountDTO;
import com.abhi.webclientnew.exception.AppException;
import com.fasterxml.jackson.core.JsonProcessingException;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutionException;

public interface AccountSvcOtherClient {
    AccountDTO add(AddAccountDTO addAccountDTO) throws IOException, AppException, InterruptedException, ExecutionException;
    AccountDTO get(String accountId) throws AppException, IOException, InterruptedException, ExecutionException;
    List<AccountDTO> getAccounts() throws IOException, InterruptedException, AppException, ExecutionException;
}
