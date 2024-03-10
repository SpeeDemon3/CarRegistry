package com.aruiz.CarRegistry.service.impl;

import com.aruiz.CarRegistry.domain.Brand;
import com.aruiz.CarRegistry.entity.BrandEntity;
import com.aruiz.CarRegistry.repository.BrandRepository;
import com.aruiz.CarRegistry.service.converter.BrandConverter;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BrandServiceImplTest {

    @InjectMocks
    private BrandServiceImpl brandService;

    @Mock
    private BrandRepository brandRepository;

    @Mock
    private BrandConverter brandConverter;


    @Test
    void test_FindByIdWhenBrandExists() throws Exception {
        // Given
        // Ojetos necesarios para la prueba
        Integer brandId = 1;
        BrandEntity brandEntity = new BrandEntity();
        brandEntity.setId(brandId);
        Brand expectedBrand = new Brand();
        expectedBrand.setId(brandId);

        // Mocking behavior
        // Configuramos comportamiento simulado de los mocks
        when(brandRepository.findById(brandId)).thenReturn(Optional.of(brandEntity));
        when(brandConverter.toBrand(brandEntity)).thenReturn(expectedBrand);

        // When
        // Ejecutamos el método que queremos probar
        Brand actualBrand = brandService.findById(brandId);

        // Then
        // Verificamos que el resultado del método sea el esperado y que se hayan llamado los métodos mockeado en las cantidades esperadas
        assertEquals(expectedBrand, actualBrand);
        verify(brandRepository, times(1)).findById(brandId);
        verify(brandConverter, times(1)).toBrand(brandEntity);
    }


    @Test
    void test_findByIdWhenBrandDoesNotExist() throws Exception {
        // Given
        // ID de marca que queremos utilizar en la prueba
        int brandId = 1;

        // Mocking behavior
        // Simulamos que no se encuentra ninguna entidad de marca en el repositorio para el ID proporcionado
        when(brandRepository.findById(brandId)).thenReturn(Optional.empty());

        // When
        // Ejecutamos el método que queremos probar
        Brand actualBrand = brandService.findById(brandId);

        // Then
        // Verificamos que el resultado del método sea nulo ya que no se encontró ninguna marca con el ID proporcionado
        assertEquals(null, actualBrand);
        // Verificamos que el método findById fue llamado una vez con el ID proporcionado
        verify(brandRepository, times(1)).findById(brandId);
        // Verificamos que el método toBrand nunca fue llamado, ya que no se encontró ninguna entidad de marca en el repositorio
        verify(brandConverter, never()).toBrand(any());
    }


    @Test
    void test_save() throws Exception {

        // Given
        Brand brandRequest = new Brand();
        brandRequest.setId(1);
        brandRequest.setName_brand("Test");
        brandRequest.setWarranty(3);
        brandRequest.setCountry("Spain");

        BrandEntity brandEntity = new BrandEntity();
        brandEntity.setId(1);
        brandEntity.setName_brand("Test");
        brandEntity.setWarranty(3);
        brandEntity.setCountry("Spain");

        // Configuramos el comportamiento del mock brandConverter
        when(brandConverter.toBrandEntity(brandRequest)).thenReturn(brandEntity); // Mock para convertir Brand a BrandEntity
        when(brandRepository.save(brandEntity)).thenReturn(brandEntity); // Mock para guardar la entidad
        when(brandConverter.toBrand(brandEntity)).thenReturn(brandRequest); // Mock para convertir BrandEntity a Brand

        // When
        // Llamada al método a probar
        Brand savedBrand = brandService.save(brandRequest);

        // Then
        // Verificamos que el resultado devuelto sea el mismo que el objeto de dominio de entrada
        assertEquals(brandRequest, savedBrand);
        // Verificamos que el método save del repositorio fue llamado una vez con la entidad correcta
        verify(brandRepository, times(1)).save(brandEntity);
        // Verificamos que el método toBrand de brandConverter se llamó una vez con la entidad devuelta por brandRepository
        verify(brandConverter, times(1)).toBrand(brandEntity);

    }

    @Test
    void test_delete() throws Exception {
        // Given
        Integer brandId = 1;

        // Configuramos el comportamiento del mock brandRepository
        when(brandRepository.findById(brandId)).thenReturn(Optional.of(new BrandEntity())); // Mock para devolver una entidad válida al buscar por ID

        // When
        boolean isDeleted = brandService.delete(brandId); // Llamada al método a probar

        // Then
        assertTrue(isDeleted); // Verificar que se eliminó correctamente
        verify(brandRepository, times(1)).findById(brandId); // Verificar que se llamó a findById en el repositorio
        verify(brandRepository, times(1)).deleteById(brandId); // Verificar que se llamó a deleteById en el repositorio
    }

    @Test
    void test_deleteWhenBrandNotExists() throws Exception {
        // Given
        Integer brandId = 1;

        // Configuramos el comportamiento del mock brandRepository para devolver un Optional vacío al buscar por ID
        when(brandRepository.findById(brandId)).thenReturn(Optional.empty());

        // When
        boolean isDeleted = brandService.delete(brandId); // Llamada al método a probar

        // Then
        assertFalse(isDeleted); // Verificar que no se eliminó porque la entidad no existe
        verify(brandRepository, times(1)).findById(brandId); // Verificar que se llamó a findById en el repositorio
        verify(brandRepository, never()).deleteById(brandId); // Verificar que no se llamó a deleteById en el repositorio
    }

    @Test
    void test_updateBrandWhenBrandExists() throws Exception {
        // Given
        Integer brandId = 1;
        Brand brandRequest = new Brand(); // Crea una nueva instancia de Brand para la solicitud de actualización
        brandRequest.setId(brandId);
        BrandEntity existingBrandEntity = new BrandEntity(); // Crea una instancia de BrandEntity existente
        existingBrandEntity.setId(brandId); // Establece el ID en la entidad existente

        // Configuramos el comportamiento del mock brandRepository para devolver la entidad existente al buscar por ID
        when(brandRepository.findById(brandId)).thenReturn(Optional.of(existingBrandEntity));

        // Mock para el objeto de entidad resultante después de la actualización
        BrandEntity updatedBrandEntity = new BrandEntity();
        updatedBrandEntity.setId(brandId);
        when(brandConverter.toBrandEntity(brandRequest)).thenReturn(updatedBrandEntity);

        // When
        Brand updatedBrand = brandService.updateBrand(brandId, brandRequest); // Llamada al método a probar
        updatedBrand.setId(brandId);

        // Then
        assertNotNull(updatedBrand); // Verifica que se devuelve un objeto de marca actualizado
        assertEquals(brandId, updatedBrand.getId()); // Verifica que el ID del objeto de marca actualizado sea el mismo que el ID proporcionado
        verify(brandRepository, times(1)).findById(brandId); // Verifica que se llamó a findById en el repositorio
        verify(brandRepository, times(1)).save(updatedBrandEntity); // Verifica que se llamó a save en el repositorio con la entidad actualizada
    }

    @Test
    void test_updateBrandWhenBrandNotExists() throws Exception {
        // Given
        Integer brandId = 1;
        Brand brandRequest = new Brand(); // Crea una nueva instancia de Brand para la solicitud de actualización

        // Configuramos el comportamiento del mock brandRepository para devolver un Optional vacío al buscar por ID
        when(brandRepository.findById(brandId)).thenReturn(Optional.empty());

        // When
        Brand updatedBrand = brandService.updateBrand(brandId, brandRequest); // Llamada al método a probar

        // Then
        assertNull(updatedBrand); // Verifica que no se devuelve ningún objeto de marca, ya que no existe la marca a actualizar
        verify(brandRepository, times(1)).findById(brandId); // Verifica que se llamó a findById en el repositorio
        verify(brandRepository, never()).save(any()); // Verifica que no se llamó a save en el repositorio (ya que la marca no existe)
    }


}