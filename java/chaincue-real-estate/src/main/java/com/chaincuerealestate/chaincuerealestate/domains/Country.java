package com.chaincuerealestate.chaincuerealestate.domains;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity(name = "country")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Country {
    @Id
    private String id;
    private String name;

    public static Country create(CountryNames name) {
        return new Country(
                UUID.randomUUID().toString(),
                name.toString()
        );
    }

    public enum CountryNames {
        SWEDEN,
        SPAIN
    }
}
