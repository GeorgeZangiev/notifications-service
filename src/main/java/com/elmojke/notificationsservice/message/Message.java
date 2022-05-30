package com.elmojke.notificationsservice.message;

import com.elmojke.notificationsservice.enums.MessageStatus;
import lombok.*;

import javax.persistence.*;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Message {
    private UUID id;
    @NonNull
    private String sendingDateTime;
    @Enumerated(EnumType.STRING)
    @NonNull
    private MessageStatus messageStatus;
    @NonNull
    private Integer assignedNotificationId;
    @NonNull
    private Integer assignedCustomerId;
}
