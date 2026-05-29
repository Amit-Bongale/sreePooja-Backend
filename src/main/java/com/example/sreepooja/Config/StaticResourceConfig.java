package com.example.sreepooja.Config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.nio.file.Paths;

@Configuration
public class StaticResourceConfig
        implements WebMvcConfigurer {

    @Value("${app.upload.base-path}")
    private String uploadPath;

    @Override
    public void addResourceHandlers(
            ResourceHandlerRegistry registry) {

        registry.addResourceHandler("/uploads/**")
                .addResourceLocations(
                        "file:" +
                                Paths.get(uploadPath)
                                        .toAbsolutePath()
                                        .toString()
                                + "/"
                );
    }
}