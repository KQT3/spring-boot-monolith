package com.chaincuerealestate.chaincuerealestate.services;

import com.chaincuerealestate.chaincuerealestate.domains.HouseImage;
import com.chaincuerealestate.chaincuerealestate.repositories.HouseImageRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class HouseImageServiceTest {
    @InjectMocks
    HouseImageService houseImageService;
    @Mock
    HouseImageRepository houseImageRepository;

    @Test
    void save() {
        // Given
        String url = "url";
        HouseImage houseImage = HouseImage.create(url);
        Mockito.when(houseImageRepository.save(Mockito.any(HouseImage.class))).thenReturn(houseImage);

        // When
        HouseImage save = houseImageService.save(url);

        // Then
        Mockito.verify(houseImageRepository, Mockito.times(1)).save(Mockito.any(HouseImage.class));
    }
}
