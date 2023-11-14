package com.chaincuerealestate.chaincuerealestatereactive.exceptions

class HouseNotFoundException(id: String) : RuntimeException(String.format("House not found. Id: %s", id))
