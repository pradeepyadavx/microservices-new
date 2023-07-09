package com.pradeep.notificationservice;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.consumer.Consumer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.annotation.TopicPartition;
import org.springframework.kafka.core.ConsumerFactory;

@SpringBootApplication
@Slf4j
public class NotificationServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(NotificationServiceApplication.class, args);
    }

    @KafkaListener(topics = "notificationTopic")
    public void handleNotification(OrderPlacedEvent orderPlacedEvent) {
        // send out an email notification
        log.info("Received Notification for Order - {}", orderPlacedEvent.getOrderNumber());
    }



//    @KafkaListener(topicPartitions = @TopicPartition(topic = "so56114299",
//            partitions = "#{@finder.partitions('so56114299')}"))
//    public void handleNotification(OrderPlacedEvent orderPlacedEvent) {
//        // send out an email notification
//        log.info("Received Notification for Order - {}", orderPlacedEvent.getOrderNumber());
//    }
//
//    @Bean
//    public PartitionFinder finder(ConsumerFactory<String, String> consumerFactory) {
//        return new PartitionFinder(consumerFactory);
//    }
//
//    public static class PartitionFinder {
//
//        public PartitionFinder(ConsumerFactory<String, String> consumerFactory) {
//            this.consumerFactory = consumerFactory;
//        }
//
//        private final ConsumerFactory<String, String> consumerFactory;
//
//        public String[] partitions(String topic) {
//            try (Consumer<String, String> consumer = consumerFactory.createConsumer()) {
//                return consumer.partitionsFor(topic).stream()
//                        .map(pi -> "" + pi.partition())
//                        .toArray(String[]::new);
//            }
//        }
//    }


}