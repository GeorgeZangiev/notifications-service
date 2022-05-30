package com.elmojke.notificationsservice.notification;

import com.elmojke.notificationsservice.message.MessageService;
import com.elmojke.notificationsservice.requests.NotificationRequest;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/notification")
@AllArgsConstructor
@Slf4j
public class NotificationController {

    private final NotificationService notificationService;
    private final MessageService messageService;

    @GetMapping
    public List<Notification> getAllNotifications(){
        log.info("getting all notifications");
        return notificationService.getAllNotifications();
    }

    @PostMapping("/create-notification")
    public void sendNotification(@RequestBody NotificationRequest notificationRequest) {
        log.info("New notification... {}", notificationRequest);
        Notification notification = notificationService.sendNotification(notificationRequest);
        messageService.sendMessage(notification);
    }

    @DeleteMapping(path = "{notificationId}")
    public void deleteNotification(
            @PathVariable("notificationId") Integer notificationId) {
        log.info("deleting notification with id: {}", notificationId);
        notificationService.deleteNotification(notificationId);
    }

    @PutMapping(path = "{notificationId}")
    public void updateNotification(
            @PathVariable("notificationId") Integer notificationId,
            @RequestBody Notification notificationDetails){
        log.info("updating notification with id: {}", notificationId);
        notificationService.updateNotification(notificationId, notificationDetails);
        log.info("updating messages for notification with id: {}", notificationId);
        messageService.sendMessage(notificationDetails);
    }
}
