package com.chaincuerealestate.chaincuerealestate.routes.house;

public record HousePageDTO(
        String id,
        String title,
        String type,
        String location,
        int numberOfRooms,
        int beds,
        String dollarPrice,
        String cryptoPrice,
        String description,
        HouseImage[] images,
        Broker broker
) {

    public record HouseImage(
            String id,
            String url
    ) {
    }

    public record Broker(
            String id,
            String name,
            String phoneNumber,
            String email
    ) {
    }

}
