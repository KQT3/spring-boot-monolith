package com.example.chaincuerealestate.services

import com.example.chaincuerealestate.domains.House

interface HouseServiceI {
    fun save(houseTypes: House.HouseTypes): House
    fun findById(id: String): House
    fun findAll(): List<House>
}

