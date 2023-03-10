package com.abhi.webclientnew.mapper;

import com.abhi.webclientnew.dto.AccountDTO;
import com.abhi.webclientnew.dto.UpdateAccountDTO;
import org.mapstruct.Mapper;
import org.springframework.stereotype.Component;

@Mapper(componentModel = "spring")
public interface AccountMapper {
    UpdateAccountDTO accToUpdAcc(AccountDTO accountDTO);

}
