package com.chen.bitten.server;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication(scanBasePackages = {"com.chen"})
@MapperScan("com.chen.**.mapper")
public class BittenApplication {
    public static void main(String[] args) {
        SpringApplication.run(BittenApplication.class, args);
    }
}
