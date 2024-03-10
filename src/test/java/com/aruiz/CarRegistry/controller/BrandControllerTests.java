package com.aruiz.CarRegistry.controller;

import com.aruiz.CarRegistry.controller.dto.BrandRequest;
import com.aruiz.CarRegistry.controller.mapper.BrandMapper;
import com.aruiz.CarRegistry.domain.Brand;
import com.aruiz.CarRegistry.service.BrandService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

public class BrandControllerTests {
    @Mock
    private BrandService brandService;

    @Mock
    private BrandMapper brandMapper;

    @InjectMocks
    private BrandController brandController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void test_addBrand() throws Exception {
        // Given
        // Solicitud de marca vacía para simular una solicitud de marca válida
        BrandRequest brandRequest = new BrandRequest();
        // Mockeamos el comportamiento del servicio para que retorne una nueva instancia de Brand al guardar
        when(brandService.save(any())).thenReturn(new Brand());
        // When
        // Llamo al método addBrand del controlador de marcas
        ResponseEntity<?> responseEntity = brandController.addBrand(brandRequest);
        // Then
        // Verifico que el código de estado de la respuesta sea 200 (OK)
        assertEquals(200, responseEntity.getStatusCodeValue());
    }

    @Test
    void test_addBrands() throws Exception {
        // Given
        // Lista vacía de solicitudes de marca para simular una lista de marcas válidas
        List<BrandRequest> brandRequestList = new ArrayList<>();
        // Mockeamos el comportamiento del servicio para que retorne una lista vacía de marcas al guardar
        when(brandService.saveAll(any())).thenReturn(CompletableFuture.completedFuture(new ArrayList<>()));
        // When
        // Llamamos al método addBrands del controlador de marcas
        CompletableFuture<?> completableFuture = brandController.addBrands(brandRequestList);
        // Then
        // Verificamos que el código de estado de la respuesta sea 200 (OK)
        assertEquals(200, ((ResponseEntity<?>) completableFuture.get()).getStatusCodeValue());
    }

    @Test
    void test_getBrandById() throws Exception {
        // Given
        // Definimos un ID de marca
        int brandId = 1;
        // Mockeamos el comportamiento del servicio para que retorne una nueva instancia de Brand
        when(brandService.findById(brandId)).thenReturn(new Brand());
        // When
        // Llamamos al método getBrandById del controlador de marcas
        ResponseEntity<?> responseEntity = brandController.getBrandById(brandId);
        // Then
        // Verificamos que el código de estado de la respuesta sea 200 (OK)
        assertEquals(200, responseEntity.getStatusCodeValue());
    }

    @Test
    void test_getBrands_notFound() throws Exception {
        // Mockeamos el comportamiento del servicio para que retorne una lista vacía de marcas al buscar todas las marcas
        when(brandService.findAll()).thenReturn(CompletableFuture.completedFuture(new ArrayList<>()));
        // When
        // Llamamos al método getBrands del controlador de marcas
        CompletableFuture<?> completableFuture = brandController.getBrands();
        // Then
        // Verificamos que el código de estado de la respuesta sea 404 (Not Found) porque la lista de marcas está vacía
        assertEquals(404, ((ResponseEntity<?>) completableFuture.get()).getStatusCodeValue());
    }

    @Test
    void test_deleteBrandById() throws Exception {
        // Given
        // Definimos un ID de marca
        int brandId = 1;
        // Mockeamos el comportamiento del servicio para que retorne true al eliminar la marca con el ID proporcionado
        when(brandService.delete(brandId)).thenReturn(true);

        // Llamamos al método deleteBrandById del controlador de marcas
        ResponseEntity<?> responseEntity = brandController.deleteBrandById(brandId);
        // Then
        // Verificamos que el código de estado de la respuesta sea 200 (OK)
        assertEquals(200, responseEntity.getStatusCodeValue());
    }

    @Test
    void test_updateBrandByID() throws Exception {
        // Given
        // Definimos un ID de marca y una solicitud de marca vacía
        int brandId = 1;
        BrandRequest brandRequest = new BrandRequest();
        // Mockeamos el comportamiento del servicio para que retorne una nueva instancia de Brand al actualizar la marca con el ID proporcionado
        when(brandService.updateBrand(brandId, new Brand())).thenReturn(new Brand());
        // When
        // Llamamos al método updateBrandByID del controlador de marcas
        ResponseEntity<?> responseEntity = brandController.updateBrandByID(brandId, brandRequest);
        // Then
        // Verificamos que el código de estado de la respuesta sea 200 (OK)
        assertEquals(200, responseEntity.getStatusCodeValue());
    }
}
