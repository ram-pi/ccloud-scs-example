package com.github.rampi.ccloud;

import java.util.UUID;
import java.util.function.Consumer;
import java.util.function.Supplier;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;

import com.github.javafaker.Faker;
import com.github.rampi.ccloud.model.Sensor;

import lombok.extern.java.Log;

@SpringBootApplication
@Log
public class CcloudApplication {

	public static void main(String[] args) {
		SpringApplication.run(CcloudApplication.class, args);
	}

	@Bean
	public Supplier<Message<Sensor>> generate() {
		return () -> {
			Sensor sensor = new Sensor();
			Faker faker = new Faker();
			sensor.setId(UUID.randomUUID().toString());
			sensor.setAcceleration(faker.number().numberBetween(1, 10));
			sensor.setVelocity(faker.number().numberBetween(1, 100));
			sensor.setInternalTemperature(faker.number().numberBetween(20, 50));
			sensor.setExternalTemperature(faker.number().numberBetween(1, 30));
			log.info("Sending message: " + sensor);
			return MessageBuilder.withPayload(sensor)
					.setHeader("partitionKey", sensor.getId())
					.build();
		};
	}

	@Bean
	public Consumer<Sensor> receive() {
		return sensor -> {
			log.info("Received message: " + sensor);
		};
	}

}
