package com.aruiz.CarRegistry.controller;

import com.aruiz.CarRegistry.controller.dto.CarRequest;
import com.aruiz.CarRegistry.controller.mapper.CarMapper;
import com.aruiz.CarRegistry.domain.Car;
import com.aruiz.CarRegistry.service.CarService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

// Utilizamos @ExtendWith(MockitoExtension.class) para habilitar el uso de Mockito en las pruebas
@ExtendWith(MockitoExtension.class)
public class CarControllerTest {

    @Mock
    private CarService carService;

    @Mock
    private CarMapper carMapper;

    // Controlador bajo prueba, donde se inyectarán los mocks
    @InjectMocks
    private CarController carController;

    @Test
    void test_saveCar() throws Exception {
        // Given
        // Creamos una solicitud de coche
        CarRequest carRequest = new CarRequest();

        // Mocking behavior
        // Configuramos el comportamiento esperado del carService
        // para que cuando se llame a save con un modelo de coche convertido desde la solicitud,
        // devuelva un nuevo objeto Car
        when(carService.save(carMapper.toCarModel(carRequest))).thenReturn(new Car());

        // When
        // Llamamos al método que queremos probar en el controlador
        ResponseEntity<?> responseEntity = carController.addCar(carRequest);

        // Then
        // Verificamos que la respuesta tenga un código de estado OK (200)
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());

        // Verificamos que el método save del carService fue llamado una vez con cualquier argumento
        verify(carService, times(1)).save(any());
    }

    @Test
    void test_saveAllCars() throws Exception {
        // Given
        // Lista vacía de solicitudes de coches
        List<CarRequest> carRequestList = new ArrayList<>();

        // Mocking behavior
        // Configuramos el comportamiento esperado del carService
        // para que cuando se llame a saveAll con cualquier lista, devuelva un CompletableFuture
        // completado con una lista vacía
        when(carService.saveAll(anyList())).thenReturn(CompletableFuture.completedFuture(new ArrayList<>()));

        // When
        // Llamamos al método que queremos probar en el controlador, que devuelve un CompletableFuture
        CompletableFuture<?> completableFuture = carController.addCards(carRequestList);

        // Then
        // Esperamos a que el CompletableFuture se complete
        completableFuture.get(); // Espera a que el futuro se complete

        // Verificamos que el método saveAll del carService fue llamado una vez con cualquier lista
        verify(carService, times(1)).saveAll(anyList());
    }

    @Test
    void test_findAllCars() throws Exception {
        // Given
        List<Car> cars = new ArrayList<>();

        // Mocking behavior
        // Configuramos el comportamiento esperado del carService
        // para que cuando se llame a findAll, devuelva un CompletableFuture completado
        // con la lista de coches creada anteriormente
        when(carService.findAll()).thenReturn(CompletableFuture.completedFuture(cars));

        // When: Llamamos al método que queremos probar en el controlador, que devuelve un CompletableFuture
        CompletableFuture<?> completableFuture = carController.getCards();

        // Then
        completableFuture.get(); // Espera a que el futuro se complete
        // Verificamos que el método findAll del carService fue llamado una vez
        verify(carService, times(1)).findAll();
    }

    @Test
    void test_findCarByIdWhenExists() throws Exception {
        // Given
        int carId = 1;
        Car car = new Car();

        // Mocking behavior
        // Configuramos el comportamiento esperado del carService
        // para que cuando se llame a findById con el ID proporcionado, devuelva el objeto Car creado
        when(carService.findById(carId)).thenReturn(car);

        // When: Llamamos al método que queremos probar en el controlador
        ResponseEntity<?> responseEntity = carController.getCardById(carId);

        // Then: Verificamos que la respuesta sea HttpStatus.OK
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        // Verificamos que el objeto Car devuelto sea el mismo que el creado
        assertEquals(car, responseEntity.getBody());
        // Verificamos que el método findById del carService fue llamado una vez
        // con el ID del coche proporcionado
        verify(carService, times(1)).findById(carId);
    }

    @Test
    void test_deleteCarById() throws Exception {
        // Given
        int carId = 1;

        // Mocking behavior
        // Comportamiento esperado del carService
        // para que cuando se llame a deleteById con el ID proporcionado, devuelva "true"
        when(carService.deleteById(carId)).thenReturn(String.valueOf(true));

        // When: Llamamos al método que queremos probar en el controlador
        ResponseEntity<?> responseEntity = carController.deleteCarByID(carId);

        // Then: Verificamos que la respuesta sea HttpStatus.OK
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        // Verificamos que el método deleteById del carService fue llamado una vez
        // con el ID del coche proporcionado
        verify(carService, times(1)).deleteById(carId);
    }

    @Test
    void test_updateCarById() throws Exception {
        // Given
        int carId = 1;
        CarRequest carRequest = new CarRequest();

        Car car = new Car();
        car.setId(carId);

        // Mocking behavior
        // Comportamiento esperado del carMapper
        when(carMapper.toCarModel((carRequest))).thenReturn(car);

        // When
        // When: Llamamos al método que queremos probar en el controlador
        ResponseEntity<?> responseEntity = carController.updateCarByID(carId, carRequest);

        // Then: Verificamos que la respuesta sea HttpStatus.OK
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        // Verificamos que el método updateById del carService fue llamado una vez
        // con los parámetros correctos (carId, car)
        verify(carService, times(1)).updateById(carId, car);
    }

}
