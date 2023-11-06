package com.chaincuerealestate.chaincuerealestate.services.DTOBuilderHelpers;

import com.chaincuerealestate.chaincuerealestate.domains.Country;
import com.chaincuerealestate.chaincuerealestate.domains.House;
import com.chaincuerealestate.chaincuerealestate.services.CountryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Function;

@RequiredArgsConstructor
@Component
public class CountryHelper {
    private final CountryService countryService;

    public <B> Function<B, B> updateDTOBuilderWithCountries(BiConsumer<B, List<Country>> setCountries) {
        return dtoBuilder -> {
            var countries = countryService.findAll();
            setCountries.accept(dtoBuilder, countries);
            return dtoBuilder;
        };
    }
}
