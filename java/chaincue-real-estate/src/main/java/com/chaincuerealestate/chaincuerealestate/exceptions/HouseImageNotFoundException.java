package com.chaincuerealestate.chaincuerealestate.exceptions;

public class HouseImageNotFoundException extends RuntimeException {
    public HouseImageNotFoundException(String id) {
        super(String.format("house image not found. Id: %s", id));
    }
}
