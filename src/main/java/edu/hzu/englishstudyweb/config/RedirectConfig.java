package edu.hzu.englishstudyweb.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class RedirectConfig implements WebMvcConfigurer {

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/collection").setViewName("collection");
        registry.addViewController("/book").setViewName("book");
        registry.addViewController("/login").setViewName("login");
        registry.addViewController("/").setViewName("study");
        registry.addViewController("/study").setViewName("study");
        registry.addViewController("/review").setViewName("review");
        registry.addViewController("/error").setViewName("error");
    }
}
