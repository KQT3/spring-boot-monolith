package com.chaincuerealestate.chaincuerealestate.services;

import com.chaincuerealestate.chaincuerealestate.domains.House;
import com.chaincuerealestate.chaincuerealestate.domains.HouseImage;
import com.chaincuerealestate.chaincuerealestate.repositories.HouseImageRepository;
import com.chaincuerealestate.chaincuerealestate.repositories.HouseRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class HouseServiceTest {
    @InjectMocks
    HouseService houseService;
    @Mock
    HouseRepository houseRepository;

    @Test
    void save() {
        // Given
        House.HouseTypes houseTypes = House.HouseTypes.CONDOMINIUM;
        House houseImage = House.create(houseTypes, "");
        Mockito.when(houseRepository.save(Mockito.any(House.class))).thenReturn(houseImage);

        // When
        House save = houseService.save(houseTypes);
        System.out.println(save);
        // Then
        Mockito.verify(houseRepository, Mockito.times(1)).save(Mockito.any(House.class));
    }
}
