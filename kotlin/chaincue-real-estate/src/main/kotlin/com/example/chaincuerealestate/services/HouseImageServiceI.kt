package com.example.chaincuerealestate.services

import com.example.chaincuerealestate.domains.HouseImage

interface HouseImageServiceI {
    fun save(url: String): HouseImage
    fun findById(id: String): HouseImage
    fun findAll(): List<HouseImage>
}
