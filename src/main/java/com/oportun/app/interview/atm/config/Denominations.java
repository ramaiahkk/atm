package com.oportun.app.interview.atm.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
@ComponentScan(basePackages = "com.oportun.app.interview.atm")
@ConfigurationProperties(prefix = "atm.denominations")
public class Denominations {


    public List<Long> getAllowedCurrencies() {
        return allowedCurrencies;
    }

    public void setAllowedCurrencies(List<Long> allowedCurrencies) {
        this.allowedCurrencies = allowedCurrencies;
    }

    private List<Long> allowedCurrencies;


}
