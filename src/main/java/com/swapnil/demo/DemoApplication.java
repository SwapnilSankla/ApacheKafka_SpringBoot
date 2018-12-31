package com.swapnil.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.Date;

@SpringBootApplication
public class DemoApplication implements CommandLineRunner {

  @Autowired
  SimpleKafkaProducer simpleKafkaProducer;

	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
	}

  @Override
  public void run(String... args) throws Exception {
    simpleKafkaProducer.send("Hey, This is message from Spring boot app sent at time :" + new Date().toString());
  }
}
