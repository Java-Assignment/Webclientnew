package com.abhi.webclientnew.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AddAccountDTO {

    private AccountType accountType;

    private String customerName;

    private List<String> notes;

    private AccountStatus accountStatus;

    private String region;

}
