package com.example.oa.common;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.io.File;

@Configuration
public class WebAppConfig implements WebMvcConfigurer {

    @Value("${Constant.path}")
    private String filePath;//="D:/img"

    //映射浏览器可以直接访问的本地路径
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        if (!filePath.endsWith(File.separator)) {
            filePath = filePath + File.separator;
        }
        registry.addResourceHandler("/common/file/**").addResourceLocations("file:" + filePath);
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowCredentials(true)
                .allowedOriginPatterns("*")
                .allowedMethods("GET", "POST").allowedHeaders("*").maxAge(3600 * 24);
        WebMvcConfigurer.super.addCorsMappings(registry);
    }
}