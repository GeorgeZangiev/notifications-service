package com.elmojke.notificationsservice.notification;

import com.elmojke.notificationsservice.exception.ClientNotFoundException;
import com.elmojke.notificationsservice.exception.NotificationNotFoundException;
import com.elmojke.notificationsservice.notification.rabbitmq.RabbitMQMessageProducer;
import com.elmojke.notificationsservice.requests.NotificationRequest;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
@AllArgsConstructor
@Slf4j
public class NotificationService {

    private final NotificationRepository notificationRepository;

    public List<Notification> getAllNotifications() {
        log.info("total number of messages is {}, {} messages sent, {} messages delivered, {} messages failed",
                RabbitMQMessageProducer.getTotalCounter(), RabbitMQMessageProducer.getSentCounter(),
                RabbitMQMessageProducer.getDeliveredCounter(), RabbitMQMessageProducer.getFailedCounter());
        return notificationRepository.findAll();
    }

    public Notification sendNotification(NotificationRequest notificationRequest) {
        DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        LocalDateTime startOfNotification = LocalDateTime.parse(notificationRequest.startOfNotification(), format);
        LocalDateTime endOfNotification = LocalDateTime.parse(notificationRequest.endOfNotification(), format);
        Notification notification = Notification.builder()
                .startOfNotification(startOfNotification)
                .message(notificationRequest.message())
                .operatorCode(notificationRequest.operatorCode())
                .clientTag(notificationRequest.clientTag())
                .endOfNotification(endOfNotification)
                .build();
        notificationRepository.save(notification);
        return notification;
    }

    public ResponseEntity<Notification> updateNotification(Integer notificationId, Notification notificationDetails) {
        Notification notification = notificationRepository.findById(notificationId)
                .orElseThrow(() -> {
                    log.info("notification with id " + notificationId + " does not exist");
                    return new ClientNotFoundException();
                });
            notification.setStartOfNotification(notificationDetails.getStartOfNotification());
            notification.setMessage(notificationDetails.getMessage());
            notification.setOperatorCode(notificationDetails.getOperatorCode());
            notification.setClientTag(notificationDetails.getClientTag());
            notification.setEndOfNotification(notificationDetails.getEndOfNotification());
            Notification updatedNotification = notificationRepository.save(notification);
            return ResponseEntity.ok(updatedNotification);
    }

    public void deleteNotification(Integer notificationId) {
        if(!notificationRepository.existsById(notificationId)) {
            log.info("notification with id " + notificationId + " does not exist");
            throw new NotificationNotFoundException();
        }
        else notificationRepository.deleteById(notificationId);
    }
}
