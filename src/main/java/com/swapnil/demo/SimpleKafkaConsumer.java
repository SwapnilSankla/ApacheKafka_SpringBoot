package com.swapnil.demo;

import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
@Slf4j
public class SimpleKafkaConsumer {
  @KafkaListener(topics = "${topicName}")
  void receive(String message) {
    log.info("Received message: {}. It is received at {}", message, new Date().toString());
  }
}
