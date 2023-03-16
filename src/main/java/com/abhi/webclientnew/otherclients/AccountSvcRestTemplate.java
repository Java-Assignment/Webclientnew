package com.abhi.webclientnew.otherclients;

import com.abhi.webclientnew.config.AppProperties;
import com.abhi.webclientnew.dto.AccountDTO;
import com.abhi.webclientnew.dto.AddAccountDTO;
import com.abhi.webclientnew.exception.AppException;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.List;

@Component("resttemplate")
@Slf4j
public class AccountSvcRestTemplate implements AccountSvcOtherClient {
    private AppProperties appProperties;
    private RestTemplate restTemplate;
    private HttpHeaders headers;



    public AccountSvcRestTemplate(AppProperties appProperties) {
        this.appProperties = appProperties;
        restTemplate=new RestTemplate();
        headers=new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
    }
    @Override
    public AccountDTO add(AddAccountDTO addAccountDTO) throws  AppException {
        HttpEntity<AddAccountDTO> request=new HttpEntity<>(addAccountDTO,headers);
        ResponseEntity<AccountDTO> response=restTemplate.exchange(appProperties.getAcsvc(), HttpMethod.POST,request, AccountDTO.class);
        if(response.getStatusCode().is2xxSuccessful()){
            AccountDTO newAc=response.getBody();
            log.info("AccountSvcRestTemplate NEW Ac"+newAc);
            return newAc;
        }
        else {
            throw  new AppException("AccountSvcRestTemplate.Add account failed");
        }
    }

    @Override
    public AccountDTO get(String accountId) throws AppException {
        try {
            AccountDTO getAc = restTemplate.getForObject(appProperties.getAcsvc() + "/" + accountId, AccountDTO.class);
            log.info("AccountSvcRestTemplate GET Ac:" + getAc);
            return getAc;
        }catch (HttpClientErrorException exception){
            if(exception.getStatusCode().equals(HttpStatus.NOT_FOUND)){
                return null;
            }else {
                throw new AppException("Error in getting account information. ac:"+accountId);
            }
        }
    }

    @Override
    public List<AccountDTO> getAccounts() {
        AccountDTO [] getAccounts=restTemplate.getForObject(appProperties.getAcsvc(), AccountDTO[].class);
        log.info("AccountSvcRestTemplate GET Ac size :"+getAccounts.length);
        return Arrays.asList(getAccounts);
    }


}
