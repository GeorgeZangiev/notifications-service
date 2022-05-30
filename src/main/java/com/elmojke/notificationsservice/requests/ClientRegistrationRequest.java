package com.elmojke.notificationsservice.requests;

import com.elmojke.notificationsservice.enums.ClientTag;

public record ClientRegistrationRequest(String phoneNumber, ClientTag clientTag, String timeZone) {
}
