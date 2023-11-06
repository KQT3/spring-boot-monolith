package com.chaincuerealestate.chaincuerealestate.exceptions;

public class BrokerNotFoundException extends RuntimeException {
    public BrokerNotFoundException(String id) {
        super(String.format("broker not found. Id: %s", id));
    }
}
