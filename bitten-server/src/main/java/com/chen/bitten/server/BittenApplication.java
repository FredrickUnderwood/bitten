package com.chen.bitten.server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication(scanBasePackages = {"com.chen"})
public class BittenApplication {
    public static void main(String[] args) {
        SpringApplication.run(BittenApplication.class, args);
    }
}
