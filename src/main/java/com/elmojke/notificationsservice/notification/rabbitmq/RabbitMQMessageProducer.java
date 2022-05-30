package com.elmojke.notificationsservice.notification.rabbitmq;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@AllArgsConstructor
public class RabbitMQMessageProducer {

    private final AmqpTemplate amqpTemplate;
    @Getter
    @Setter
    private static int sentCounter, deliveredCounter, failedCounter = 0;
    @Getter
    private static int totalCounter = sentCounter + deliveredCounter + failedCounter;
    public void publish(Object payload) {
        log.info("Publishing to {} using routingKey {}. Payload: {}", RabbitMQConfig.EXCHANGE, RabbitMQConfig.ROUTING_KEY, payload);
        amqpTemplate.convertAndSend(RabbitMQConfig.EXCHANGE, RabbitMQConfig.ROUTING_KEY, payload);
        log.info("Published to {} using routingKey {}. Payload: {}", RabbitMQConfig.EXCHANGE, RabbitMQConfig.ROUTING_KEY, payload);
    }

}
