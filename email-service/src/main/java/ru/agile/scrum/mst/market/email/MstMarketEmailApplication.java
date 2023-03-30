package ru.agile.scrum.mst.market.email;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableAutoConfiguration
public class MstMarketEmailApplication {

    public static void main(String[] args) {
        SpringApplication.run(MstMarketEmailApplication.class, args);
    }

}