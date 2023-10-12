package com.example.oa;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;

//@SpringBootApplication(exclude = {SecurityAutoConfiguration.class})
@SpringBootApplication
//@EntityScan("com.example.oa.bean")
public class OaApplication  {

    public static void main(String[] args) {
        SpringApplication.run(OaApplication.class, args);
    }

}
