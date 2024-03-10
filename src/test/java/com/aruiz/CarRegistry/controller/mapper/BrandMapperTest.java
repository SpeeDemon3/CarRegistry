package com.aruiz.CarRegistry.controller.mapper;

import com.aruiz.CarRegistry.controller.dto.BrandRequest;
import com.aruiz.CarRegistry.controller.dto.BrandResponse;
import com.aruiz.CarRegistry.domain.Brand;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BrandMapperTest {

    @Mock
    private BrandRequest brandRequest; // Mock de la solicitud de marca

    @InjectMocks
    private BrandMapper brandMapper; // Instancia de BrandMapper con el mock BrandRequest inyectado

    // Prueba para verificar la conversión de Brand a BrandResponse
    @Test
    void testToBrandResponse() {
        // Given
        Brand brand = new Brand();
        brand.setId(1);
        brand.setName_brand("Toyota");
        brand.setWarranty(2);
        brand.setCountry("Japan");

        // When
        BrandResponse brandResponse = brandMapper.toBrandResponse(brand);

        // Then
        assertEquals(brand.getId(), brandResponse.getId());
        assertEquals(brand.getName_brand(), brandResponse.getName_brand());
        assertEquals(brand.getWarranty(), brandResponse.getWarranty());
        assertEquals(brand.getCountry(), brandResponse.getCountry());
    }

    // Prueba para verificar la conversión de BrandRequest a Brand
    @Test
    void testToBrandModel() {
        // Given
        when(brandRequest.getName_brand()).thenReturn("Toyota");
        when(brandRequest.getWarranty()).thenReturn(2);
        when(brandRequest.getCountry()).thenReturn("Japan");

        // When
        Brand brand = brandMapper.toBrandModel(brandRequest);

        // Then
        assertEquals(brandRequest.getName_brand(), brand.getName_brand());
        assertEquals(brandRequest.getWarranty(), brand.getWarranty());
        assertEquals(brandRequest.getCountry(), brand.getCountry());
    }

    // Prueba para verificar la conversión de BrandRequest a BrandResponse
    @Test
    void testToBrandResponseFromBrandRequest() {
        // Given
        when(brandRequest.getName_brand()).thenReturn("Toyota");
        when(brandRequest.getWarranty()).thenReturn(2);
        when(brandRequest.getCountry()).thenReturn("Japan");

        // When
        BrandResponse brandResponse = brandMapper.toBrandResponse(brandRequest);

        // Then
        assertEquals(null, brandResponse.getId());
        assertEquals(brandRequest.getName_brand(), brandResponse.getName_brand());
        assertEquals(brandRequest.getWarranty(), brandResponse.getWarranty());
        assertEquals(brandRequest.getCountry(), brandResponse.getCountry());
    }

}