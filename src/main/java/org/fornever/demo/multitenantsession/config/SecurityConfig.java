package org.fornever.demo.multitenantsession.config;

import org.fornever.demo.multitenantsession.filters.ZoneFilter;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.context.SecurityContextPersistenceFilter;

@EnableWebSecurity(debug = true)
@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.addFilterBefore(new ZoneFilter(), SecurityContextPersistenceFilter.class);
        http
                .authorizeRequests().anyRequest().authenticated()
                .and().formLogin();
    }
}
