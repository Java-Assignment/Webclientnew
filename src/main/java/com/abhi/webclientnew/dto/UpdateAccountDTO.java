package com.abhi.webclientnew.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateAccountDTO {

    private String customerName;

    private AccountStatus accountStatus;

    private String region;

}
