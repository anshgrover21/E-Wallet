package org.majorProject.TrxService.Config;

import org.majorProject.TrxService.Service.TrxService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
public class SecurityConfig {


    @Autowired
    private TrxService trxService;

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(trxService);
        authenticationProvider.setPasswordEncoder(getPSEncode());
        return authenticationProvider;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(authorize -> authorize
//                        .requestMatchers("/trx/createTrx").hasAnyAuthority("ADMIN", "USER", "SERVICE")
                        .anyRequest().permitAll() // this means other than above request are also authenticated
                ).formLogin(withDefaults()).httpBasic(withDefaults()).csrf(csrf -> csrf.disable());;
        return http.build();
    }

    @Bean//here setting up an encoding schemes
    public PasswordEncoder getPSEncode(){
        return new BCryptPasswordEncoder();
    }



}
