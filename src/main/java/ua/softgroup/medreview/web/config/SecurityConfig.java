package ua.softgroup.medreview.web.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import ua.softgroup.medreview.persistent.entity.Role;
<<<<<<< HEAD
import ua.softgroup.medreview.service.SecurityService;
=======
>>>>>>> token_auth
import ua.softgroup.medreview.web.security.AuthSuccessHandler;

@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Configuration
    @Order(1)
    public static class FormLoginWebSecurityConfigurerAdapter extends WebSecurityConfigurerAdapter {
        @Autowired
        private AuthSuccessHandler authSuccessHandler;
        @Autowired
        private UserDetailsService userDetailsService;

        @Autowired
        public void configureAuthentication(AuthenticationManagerBuilder authenticationManagerBuilder) throws Exception {
            authenticationManagerBuilder
                    .userDetailsService(this.userDetailsService)
                    .passwordEncoder(passwordEncoder());
        }

        @Bean
        @Override
        public AuthenticationManager authenticationManagerBean() throws Exception {
            return super.authenticationManagerBean();
        }

        @Bean
        public PasswordEncoder passwordEncoder() {
            return new BCryptPasswordEncoder();
        }

        @Override
        protected void configure(HttpSecurity http) throws Exception {
            http
                    .authorizeRequests()
                    .antMatchers("/css/**", "/js/**", "/fonts/**").permitAll()
                    .antMatchers("/companies", "/makeCompany", "/removeCompany").hasAuthority(Role.COMPANY.name())
                    .antMatchers("/records").hasAnyAuthority()
                    .antMatchers("/admin").hasAuthority(Role.ADMIN.name())
                    .antMatchers("/records/all").hasAuthority(Role.ADMIN.name())
                    .antMatchers("/user").hasAnyAuthority(Role.ADMIN.name(), Role.COMPANY.name())
                    .anyRequest().authenticated()
                    .and()
                    .formLogin()
                    .loginPage("/login").permitAll()
                    .successHandler(this.authSuccessHandler)
                    .and()
                    .logout()
                    .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                    .logoutSuccessUrl("/login?logout")
                    .invalidateHttpSession(true)
                    .and()
                    .userDetailsService(this.userDetailsService());
        }
    }

<<<<<<< HEAD
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                .antMatchers("/admin", "/companies", "/makeCompany", "/removeCompany").hasAuthority(Role.ADMIN.name())
                .and()
                .formLogin()
                .loginPage("/login").permitAll()
                .successHandler(this.authSuccessHandler)
                .and()
                .logout()
                .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                .logoutSuccessUrl("/login?logout")
                .invalidateHttpSession(true)
                .and()
                .userDetailsService(this.userDetailsService())
                .csrf().disable();
=======
    @Configuration
    @Order(2)
    public static class ApiWebSecurityConfigurerAdapter extends WebSecurityConfigurerAdapter {
        @Override
        protected void configure(HttpSecurity http) throws Exception {
            http
                    .csrf()
                    .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse());
        }
>>>>>>> token_auth
    }
}
