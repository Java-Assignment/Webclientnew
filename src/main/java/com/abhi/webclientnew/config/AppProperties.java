package com.abhi.webclientnew.config;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.annotation.Validated;

import javax.annotation.PostConstruct;
import javax.validation.constraints.NotNull;

@Configuration
@ConfigurationProperties(prefix = "com.abhi")
@Data
@Validated
@Slf4j
public class AppProperties {
    @NotNull(message = "Accounts service url")
    private String acsvc;

    @PostConstruct
    public void printProperties(){
        log.info(this.toString());
    }

}
