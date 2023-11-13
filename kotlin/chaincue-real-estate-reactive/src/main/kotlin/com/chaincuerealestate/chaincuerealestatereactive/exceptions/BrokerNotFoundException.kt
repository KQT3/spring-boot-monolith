package com.example.chaincuerealestate.exceptions

class BrokerNotFoundException(id: String) : RuntimeException(String.format("broker not found. Id: %s", id))

