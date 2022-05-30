package com.elmojke.notificationsservice.client;

import com.elmojke.notificationsservice.enums.ClientTag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ClientRepository extends JpaRepository<Client, Integer> {
    @Query
    Boolean existsByPhoneNumber(String phoneNumber);
    @Query
    List<Client> findClientsByOperatorCodeAndClientTag(String operatorCode, ClientTag clientTag);
}
