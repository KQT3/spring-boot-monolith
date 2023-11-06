package com.chaincuerealestate.chaincuerealestate.routes.home;

public record HomePageDTO(
        Country[] countries,
        House[] recentlyAddedHouses
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
