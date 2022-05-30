package com.elmojke.notificationsservice.message;

import com.elmojke.notificationsservice.client.Client;
import com.elmojke.notificationsservice.client.ClientRepository;
import com.elmojke.notificationsservice.enums.MessageStatus;
import com.elmojke.notificationsservice.notification.Notification;
import com.elmojke.notificationsservice.notification.rabbitmq.RabbitMQMessageProducer;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;

@Service
@Slf4j
@AllArgsConstructor
public class MessageService {

    private final ClientRepository clientRepository;
    private final RabbitMQMessageProducer rabbitMQMessageProducer;

    @Getter
    @Setter
    private static int notificationSentCounter, notificationDeliveredCounter, notificationFailedCounter = 0;
    @Getter
    private static int notificationTotalCounter = notificationSentCounter + notificationDeliveredCounter + notificationFailedCounter;

    public void sendMessage(Notification notification) {
        String[] time = LocalDateTime.now().toString().split("T");
        String dateTime = time[0] + " " + time[1].substring(0, 5);
        log.info("sending the message to clients at {}", notification.getStartOfNotification());
        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                for (Client client: clientRepository.findClientsByOperatorCodeAndClientTag
                    (notification.getOperatorCode(), notification.getClientTag())) {
                        Message msg = Message.builder()
                            .id(UUID.randomUUID())
                            .sendingDateTime(dateTime)
                            .messageStatus(MessageStatus.SENT)
                            .assignedNotificationId(notification.getId())
                            .assignedCustomerId(client.getId())
                            .build();
                        if (msg.getMessageStatus().equals(MessageStatus.SENT)){
                            RabbitMQMessageProducer.setSentCounter(RabbitMQMessageProducer.getSentCounter() + 1);
                            notificationSentCounter++;
                        }
                        else if (msg.getMessageStatus().equals(MessageStatus.FAILED)){
                            RabbitMQMessageProducer.setFailedCounter(RabbitMQMessageProducer.getFailedCounter() + 1);
                            notificationFailedCounter++;
                        }
                        rabbitMQMessageProducer.publish(msg);
                    }
                log.info("total number of messages for notification with id {} is {}, {} messages sent," +
                                " {} messages delivered, {} messages failed",
                        notification.getId(), notificationTotalCounter, notificationSentCounter,
                        notificationDeliveredCounter, notificationFailedCounter);
                notificationSentCounter = notificationDeliveredCounter = notificationFailedCounter = 0;
            }
        };
        Timer timer = new Timer("executionTimer");
        long delay = ChronoUnit.MILLIS.between(LocalDateTime.now(), notification.getStartOfNotification());
        timer.schedule(timerTask, delay);
    }
}
