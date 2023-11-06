package com.chaincuerealestate.chaincuerealestate.domains;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity(name = "broker")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Broker {
    @Id
    private String id;
    private String name;
    private String phoneNumber;
    private String email;

    public static Broker create(String email) {
        return new Broker(
                UUID.randomUUID().toString(),
                "",
                "",
                email
        );
    }
}
