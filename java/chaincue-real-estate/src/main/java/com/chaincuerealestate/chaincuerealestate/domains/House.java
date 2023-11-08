package com.chaincuerealestate.chaincuerealestate.domains;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import java.util.List;
import java.util.UUID;

@Entity(name = "house")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class House {
    @Id
    private String id;
    private String title;
    private String location;
    private int numberRooms;
    private int beds;
    private String price;
    @NonNull
    private String src;
    private boolean sold;
    @NonNull
    private String houseTypes;
    @OneToMany(cascade = CascadeType.ALL)
    private List<HouseImage> images;
    @OneToOne(cascade = CascadeType.ALL)
    private Broker broker;

    /*TODO add created and mock data*/
    public enum HouseTypes {
        CONDOMINIUM,
        VILLA,
        TOWNHOUSE,
        VACATION_HOME,
        ESTATES_AND_FARMS,
        LAND,
        OTHER_HOUSES
    }

    public static House create(HouseTypes houseTypes, String src) {
        return new House(
                UUID.randomUUID().toString(),
                "",
                "",
                0,
                0,
                "",
                src,
                false,
                houseTypes.toString(),
                List.of(),
                null
        );
    }

}
