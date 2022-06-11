package com.awbd.plainsofprimus.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.sql.DataSource;

@Configuration
@EnableWebSecurity
@Profile("mysql")
public class SecurityJdbcConfig extends WebSecurityConfigurerAdapter {
    @Autowired
    private DataSource dataSource;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .logout()
                .logoutSuccessUrl("/");
        http.authorizeRequests()
                .antMatchers("/weapons/details/**").hasAnyRole("GUEST","ADMIN")
                .antMatchers("/weapons/edit/**").hasRole("ADMIN")
                .antMatchers("/weapons/new").hasRole("ADMIN")
                .antMatchers("/weapons").hasAnyRole("GUEST","ADMIN")
                .antMatchers("/weapons/update").hasRole("ADMIN")
                .antMatchers("/weapons/create").hasRole("ADMIN")
                .antMatchers("/weapons/delete/**").hasRole("ADMIN")
                .antMatchers("/weapons/options").hasAnyRole("GUEST","ADMIN")
                .antMatchers("/weapons/red/options").hasAnyRole("GUEST","ADMIN")
                .antMatchers("/weapons/use/**").hasAnyRole("GUEST","ADMIN")
                .antMatchers("/armors/details/**").hasAnyRole("GUEST","ADMIN")
                .antMatchers("/armors/edit/**").hasRole("ADMIN")
                .antMatchers("/armors/new").hasRole("ADMIN")
                .antMatchers("/armors").hasAnyRole("GUEST","ADMIN")
                .antMatchers("/armors/update").hasRole("ADMIN")
                .antMatchers("/armors/create").hasRole("ADMIN")
                .antMatchers("/armors/delete/**").hasRole("ADMIN")
                .antMatchers("/armors/options").hasAnyRole("GUEST","ADMIN")
                .antMatchers("/armors/red/options").hasAnyRole("GUEST","ADMIN")
                .antMatchers("/armors/use/**").hasAnyRole("GUEST","ADMIN")
                .antMatchers("/abilities/details/**").hasAnyRole("GUEST","ADMIN")
                .antMatchers("/abilities/edit/**").hasRole("ADMIN")
                .antMatchers("/abilities/new").hasRole("ADMIN")
                .antMatchers("/abilities").hasAnyRole("GUEST","ADMIN")
                .antMatchers("/abilities/update").hasRole("ADMIN")
                .antMatchers("/abilities/create").hasRole("ADMIN")
                .antMatchers("/abilities/delete/**").hasRole("ADMIN")
                .antMatchers("/abilities/options").hasAnyRole("GUEST","ADMIN")
                .antMatchers("/abilities/red/options").hasAnyRole("GUEST","ADMIN")
                .antMatchers("/abilities/use/**").hasAnyRole("GUEST","ADMIN")
                .antMatchers("/achievements/details/**").hasAnyRole("GUEST","ADMIN")
                .antMatchers("/achievements/edit/**").hasRole("ADMIN")
                .antMatchers("/achievements/new").hasRole("ADMIN")
                .antMatchers("/achievements").hasAnyRole("GUEST","ADMIN")
                .antMatchers("/achievements/update").hasRole("ADMIN")
                .antMatchers("/achievements/create").hasRole("ADMIN")
                .antMatchers("/achievements/delete/**").hasRole("ADMIN")
                .antMatchers("/achievements/options").hasAnyRole("GUEST","ADMIN")
                .antMatchers("/achievements/red/options").hasAnyRole("GUEST","ADMIN")
                .antMatchers("/achievements/use/**").hasAnyRole("GUEST","ADMIN")
                .and()
                .formLogin().loginPage("/login")
                .loginProcessingUrl("/authUser")
                .failureUrl("/login-error").permitAll()
                .and()
                .exceptionHandling().accessDeniedPage("/access_denied");

    }
}
