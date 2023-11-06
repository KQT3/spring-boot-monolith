package com.chaincuerealestate.chaincuerealestate.domains;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity(name = "house_image")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class HouseImage {
    @Id
    private String id;
    private String url;

    public static HouseImage create(String url) {
        return new HouseImage(
                UUID.randomUUID().toString(),
                url
        );
    }
}
