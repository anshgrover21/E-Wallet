package org.majorProject.UserService.Config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class KafkaConfig {

    @Bean
    public PasswordEncoder getPSEncode(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public WebClient getWebClient(){
        return WebClient.builder().build();
    }


    @Bean
    public ObjectMapper getObjectMapper() {
        return new ObjectMapper();
    }


}
