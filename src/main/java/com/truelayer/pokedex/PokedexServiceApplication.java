package com.truelayer.pokedex;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication(scanBasePackages = {"com.truelayer"},exclude = {DataSourceAutoConfiguration.class })
public class PokedexServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(PokedexServiceApplication.class, args);
    }

}
