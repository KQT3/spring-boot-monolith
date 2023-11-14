package com.chaincuerealestate.chaincuerealestatereactive.exceptions

class BrokerNotFoundException(id: String) : RuntimeException(String.format("broker not found. Id: %s", id))

