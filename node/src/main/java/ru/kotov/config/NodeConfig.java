package ru.kotov.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.kotov.utils.CryptoTool;

@Configuration
public class NodeConfig {
    @Value("${salt}")
    private String salt;
    @Bean
    public CryptoTool cryptoTool () {
        return new CryptoTool(salt);
    }
}
