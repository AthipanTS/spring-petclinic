// package org.springframework.samples.petclinic.config;
//
// import org.springframework.context.annotation.Configuration;
// import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
//
// @Configuration
// @EnableWebSecurity
// public class SecurityConfig extends WebSecurityConfigurerAdapter {
//
// @Override
// protected void configure(HttpSecurity http) throws Exception {
// http.csrf()
// .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
// .and()
// .authorizeRequests()
// .antMatchers("/admin/**")
// .hasRole("ADMIN")
// .anyRequest()
// .permitAll();
// }
//
// }
