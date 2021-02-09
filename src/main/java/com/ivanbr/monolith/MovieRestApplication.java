package com.ivanbr.monolith;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@SpringBootApplication
@EnableAspectJAutoProxy(proxyTargetClass = true)
public class MovieRestApplication {
    public static void main(String[] args) {
        SpringApplication.run(MovieRestApplication.class, args);
    }
}
