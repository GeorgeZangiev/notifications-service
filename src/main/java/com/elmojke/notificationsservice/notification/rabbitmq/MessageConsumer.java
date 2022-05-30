package com.elmojke.notificationsservice.notification.rabbitmq;


import com.elmojke.notificationsservice.enums.MessageStatus;
import com.elmojke.notificationsservice.message.Message;
import com.elmojke.notificationsservice.message.MessageService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
@Slf4j
public class MessageConsumer {

    @RabbitListener(queues = RabbitMQConfig.QUEUE)
    public void consumer(Message message) {
        message.setMessageStatus(MessageStatus.DELIVERED);
        log.info("Consumed {} from queue", message);
        RabbitMQMessageProducer.setDeliveredCounter(RabbitMQMessageProducer.getDeliveredCounter() + 1);
        RabbitMQMessageProducer.setSentCounter(RabbitMQMessageProducer.getSentCounter() - 1);
        MessageService.setNotificationDeliveredCounter(MessageService.getNotificationDeliveredCounter() + 1);
        MessageService.setNotificationSentCounter(MessageService.getNotificationSentCounter() - 1);
    }
}
