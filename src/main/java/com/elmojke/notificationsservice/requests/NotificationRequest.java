package com.elmojke.notificationsservice.requests;

import com.elmojke.notificationsservice.enums.ClientTag;

public record NotificationRequest(String startOfNotification, String message,
                                  String operatorCode, ClientTag clientTag, String endOfNotification) {
}
