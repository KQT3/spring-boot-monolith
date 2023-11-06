package com.chaincuerealestate.chaincuerealestate.exceptions;

public class HouseNotFoundException extends RuntimeException {
    public HouseNotFoundException(String id) {
        super(String.format("house not found. Id: %s", id));
    }
}
