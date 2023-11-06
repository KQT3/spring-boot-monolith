package com.chaincuerealestate.chaincuerealestate.services;

import com.chaincuerealestate.chaincuerealestate.domains.Country;

import java.util.List;

public interface CountryServiceI {
    Country save(Country.CountryNames countryNames);

    List<Country> findAll();
}
