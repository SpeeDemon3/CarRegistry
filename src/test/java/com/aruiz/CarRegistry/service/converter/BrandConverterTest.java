package com.aruiz.CarRegistry.service.converter;

import com.aruiz.CarRegistry.controller.dto.BrandRequest;
import com.aruiz.CarRegistry.domain.Brand;
import com.aruiz.CarRegistry.entity.BrandEntity;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class BrandConverterTest {

    @Test
    void test_toBrand() {
        // Crear una instancia mock de BrandEntity
        BrandEntity entityMock = mock(BrandEntity.class);

        // Configurar el comportamiento del mock
        when(entityMock.getId()).thenReturn(1);
        when(entityMock.getName_brand()).thenReturn("Toyota");
        when(entityMock.getWarranty()).thenReturn(2);
        when(entityMock.getCountry()).thenReturn("Japan");

        // Crear una instancia de BrandConverter
        BrandConverter converter = new BrandConverter();

        // Llamar al método toBrand y pasar el mock como argumento
        Brand brand = converter.toBrand(entityMock);

        // Verificar que los valores se hayan asignado correctamente
        assertEquals(1, brand.getId());
        assertEquals("Toyota", brand.getName_brand());
        assertEquals(2, brand.getWarranty());
        assertEquals("Japan", brand.getCountry());
    }

    @Test
    void test_toBrandEntity() {
        // Crear una instancia mock de Brand
        Brand brandMock = mock(Brand.class);

        // Configurar el comportamiento del mock
        when(brandMock.getId()).thenReturn(1);
        when(brandMock.getName_brand()).thenReturn("Toyota");
        when(brandMock.getWarranty()).thenReturn(2);
        when(brandMock.getCountry()).thenReturn("Japan");

        // Crear una instancia de BrandConverter
        BrandConverter converter = new BrandConverter();

        // Llamar al método toBrandEntity y pasar el mock como argumento
        BrandEntity entity = converter.toBrandEntity(brandMock);

        // Verificar que los valores se hayan asignado correctamente
        assertEquals(1, entity.getId());
        assertEquals("Toyota", entity.getName_brand());
        assertEquals(2, entity.getWarranty());
        assertEquals("Japan", entity.getCountry());
    }

    @Test
    void test_toBrandEntityFromRequest() {
        // Crear una instancia de BrandRequest
        BrandRequest request = new BrandRequest("Toyota", 2, "Japan");

        // Crear una instancia de BrandConverter
        BrandConverter converter = new BrandConverter();

        // Llamar al método toBrandEntity y pasar el BrandRequest como argumento
        BrandEntity entity = converter.toBrandEntity(request);

        // Verificar que los valores se hayan asignado correctamente
        assertEquals(null, entity.getId());
        assertEquals("Toyota", entity.getName_brand());
        assertEquals(2, entity.getWarranty());
        assertEquals("Japan", entity.getCountry());
    }


}