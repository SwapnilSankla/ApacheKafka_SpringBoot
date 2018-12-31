package com.swapnil.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class SimpleKafkaProducer {
  @Autowired
  KafkaTemplate<String, String> kafkaTemplate;

  @Value("${topicName}")
  private String topicName;

  public void send(String message) {
    kafkaTemplate.send(topicName, message);
  }
}
