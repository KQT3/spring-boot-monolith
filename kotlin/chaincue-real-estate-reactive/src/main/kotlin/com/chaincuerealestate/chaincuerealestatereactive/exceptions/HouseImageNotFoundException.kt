package com.chaincuerealestate.chaincuerealestatereactive.exceptions

class HouseImageNotFoundException(id: String) : RuntimeException(String.format("HouseImage not found. Id: %s", id))
