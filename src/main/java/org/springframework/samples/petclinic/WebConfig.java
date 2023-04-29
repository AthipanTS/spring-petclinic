// package org.springframework.samples.petclinic;
//
// import org.springframework.boot.web.servlet.FilterRegistrationBean;
// import org.springframework.context.annotation.Bean;
// import org.springframework.context.annotation.Configuration;
//
// @Configuration
// public class WebConfig {
//
// @Bean
// public FilterRegistrationBean<MyFilter> myFilter() {
// FilterRegistrationBean<MyFilter> registration = new FilterRegistrationBean<>();
// registration.setFilter(new MyFilter());
// registration.addUrlPatterns("/*");
// registration.setName("myFilter");
// registration.setOrder(1);
// return registration;
// }
//
// }
