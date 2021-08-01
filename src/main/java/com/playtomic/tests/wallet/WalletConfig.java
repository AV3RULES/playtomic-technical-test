package com.playtomic.tests.wallet;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class WalletConfig {

    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }
}
