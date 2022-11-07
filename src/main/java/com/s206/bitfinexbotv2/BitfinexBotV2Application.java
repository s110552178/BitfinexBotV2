package com.s206.bitfinexbotv2;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
@EnableScheduling
@SpringBootApplication
public class BitfinexBotV2Application {

	public static void main(String[] args) {
		SpringApplication.run(BitfinexBotV2Application.class, args);
	}

}
