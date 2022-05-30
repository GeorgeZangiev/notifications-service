package com.elmojke.notificationsservice.client;

import com.elmojke.notificationsservice.enums.ClientTag;
import lombok.*;
import javax.persistence.*;
import java.time.ZoneId;
import java.util.TimeZone;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Client {

    @Id
    @SequenceGenerator(
            name = "client_id_sequence",
            sequenceName = "client_id_sequence"
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "client_id_sequence"
    )
    private Integer id;
    @NonNull
    @Column(unique=true)
    private String phoneNumber;
    private String operatorCode;
    @Enumerated(EnumType.STRING)
    @NonNull
    private ClientTag clientTag;
    @NonNull
    private String timeZone;
}
