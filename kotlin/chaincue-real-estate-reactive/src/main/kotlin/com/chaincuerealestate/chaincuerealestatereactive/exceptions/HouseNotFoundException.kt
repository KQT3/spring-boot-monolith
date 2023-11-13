package com.example.chaincuerealestate.exceptions

class HouseNotFoundException(id: String) : RuntimeException(String.format("House not found. Id: %s", id))
