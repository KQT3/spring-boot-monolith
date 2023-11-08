package com.chaincuerealestate.chaincuerealestate.routes.houses;

public record HousesPageDTO(
        Country[] countries,
        House[] houses
) {

    record Country(
            String name
    ) {
    }

    record House(
            String id,
            String title,
            String location,
            int numberRooms,
            int beds,
            String dollarPrice,
            String cryptoPrice,
            String src
    ) {
    }
}
