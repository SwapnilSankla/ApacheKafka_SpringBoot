# Kafka

It is super simple to implement basic Kafka Producer and Consumer with the help of Spring.

## Setup
1. Download and install [Apache Kafka](https://kafka.apache.org/downloads)
2. Start Zookeeper using command: `./zookeeper-server-start.sh ../config/zookeeper.properties`
3. Start a broker using command: `./kafka-server-start.sh ../config/server.properties`
4. Create topic using command: `./kafka-topics.sh --zookeeper localhost:2181 --create --topic MyTopic --partitions 2 --replication-factor 1`

That's it, your single node Kafka cluster is ready to use. 
Apache Kafka comes with two useful scripts which are running console producer and consumer. These are useful to quickly validate 
the setup. 

## Run console Kafka Producer to generate messages
1. Run command `./kafka-console-producer.sh --broker-list localhost:9092 --topic MyTopic` and start sending messages

## Run console Kafka Consumer to consume messages
1. Run command `./kafka-console-consumer.sh --bootstrap-server localhost:9092 --topic MyTopic` and start consuming messages

## Spring boot application
Spring has made it super easy to configure and use Apache Kafka. Following is snippet of dependencies specified in pom.xml

```
<dependencies>
	<dependency>
		<groupId>org.springframework.boot</groupId>			
		<artifactId>spring-boot-starter</artifactId>
	</dependency>
	<dependency>
		<groupId>org.springframework.kafka</groupId>
		<artifactId>spring-kafka</artifactId>	
	</dependency>
	<dependency>
		<groupId>org.projectlombok</groupId>
		<artifactId>lombok</artifactId>
		<optional>true</optional>
	</dependency>
</dependencies>
```

1. spring-kafka dependency makes it easy to use Kafka in Java application.
2. lombok dependency is not mandatory. However I am using it to get configuration driven logger.

#### Configuration
Following is the application.yml file. Notice Spring's Kafka support wherein you need to specify producer and consumer properties.

1. bootstrap-servers: List of brokers in the cluster
2. key-serializer/ value-serializer: Every message over Kafka is sent in the form of array of bytes. This means the producer and consumer needs to serialize and deserialize actual message from array of bytes. We can specify serializer class details here.
3. key-deserializer/ value-deserializer: Deserialization class details
4. group-id: We can configure multiple consumers consuming records from same topic. group-id makes a provision to logically group consumers who are going to read from same topic. Max number of consumers cannot exceed number of partitions.

```
topicName: "MyTopic"
serializerClass: "org.apache.kafka.common.serialization.StringSerializer"
deserializerClass: "org.apache.kafka.common.serialization.StringDeserializer"
spring:
  kafka:
    bootstrap-servers: "localhost:9092"
    producer:
      key-serializer: ${serializerClass}
      value-serializer: ${serializerClass}
    consumer:
      key-deserializer: ${deserializerClass}
      value-deserializer: ${deserializerClass}
      group-id: "MyGroup"
```

Kafka package already has serialization/ deserialization support for few basic data types like String, int, double.
To keep it simple I am using String messages hence the configuration reflects corresponding Serialization and deserialization classes.

#### Kafka Producer
Use Kafka Template to send messages.

```java
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
```

#### Kafka Consumer
Consuming messages is even more easier. 

Just use annotation `@KafkaListener(topics = "${topicName}")` and you are done.


```java
@Component
@Slf4j
public class SimpleKafkaConsumer {
  @KafkaListener(topics = "${topicName}")
  void receive(String message) {
    log.info("Received message: {}. It is received at {}", message, new Date().toString());
  }
}
```
