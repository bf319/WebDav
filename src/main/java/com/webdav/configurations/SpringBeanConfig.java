package com.webdav.configurations;

import io.milton.servlet.MiltonFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.servlet.Filter;

@Configuration
public class SpringBeanConfig {

    @Bean
    public FilterRegistrationBean someFilterRegistration() {
        FilterRegistrationBean registration = new FilterRegistrationBean();
        registration.setFilter(getMiltonFilter());
        registration.setName("MiltonFilter");
        registration.addUrlPatterns("/*");
        registration.addInitParameter("resource.factory.class",
                "io.milton.http.annotated.AnnotationResourceFactory");
        registration.addInitParameter("controllerPackagesToScan",
                "com.webdav.controllers");
        registration.addInitParameter("milton.configurator",
                "com.webdav.configurations.MiltonConfig");
        registration.setOrder(1);
        return registration;
    }

    public Filter getMiltonFilter() {
        return new MiltonFilter();
    }
}