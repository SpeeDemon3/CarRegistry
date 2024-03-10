package com.aruiz.CarRegistry.service.impl;

import com.aruiz.CarRegistry.controller.dto.CarRequest;
import com.aruiz.CarRegistry.domain.Car;
import com.aruiz.CarRegistry.entity.BrandEntity;
import com.aruiz.CarRegistry.entity.CarEntity;
import com.aruiz.CarRegistry.repository.BrandRepository;
import com.aruiz.CarRegistry.repository.CarRepository;
import com.aruiz.CarRegistry.service.converter.CarConverter;
import org.apache.commons.csv.CSVRecord;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.*;

@SpringBootTest
class CarServiceImplTest {

    @Mock
    private CarRepository carRepository;

    @Mock
    private CarConverter carConverter;

    @Mock
    BrandRepository brandRepository;

    @InjectMocks
    private CarServiceImpl carService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }



    @Test
    void save() throws Exception {
        // Given
        Car car = new Car();
        CarEntity carEntity = new CarEntity();
        // Mocking behavior
        when(carConverter.toCarEntity(car)).thenReturn(carEntity);
        when(carRepository.save(carEntity)).thenReturn(carEntity);
        when(carConverter.toCar(carEntity)).thenReturn(car);
        // When
        Car savedCar = carService.save(car);
        // Then
        assertEquals(car, savedCar);
    }

    @Test
    void saveAll() throws Exception {
        // Given
        List<CarRequest> carRequestsList = new ArrayList<>();
        CarRequest carRequest = new CarRequest();
        carRequestsList.add(carRequest);

        CarEntity carEntity = new CarEntity();
        List<Car> cars = new ArrayList<>();
        cars.add(new Car());

        // Configuramos el comportamiento del convertidor para que devuelva la entidad de automóvil creada
        when(carConverter.toCarEntity(carRequest)).thenReturn(carEntity);
        // Configuramos el comportamiento del repositorio para que devuelva la misma entidad de automóvil
        when(carRepository.save(carEntity)).thenReturn(carEntity);
        // Configuramos el comportamiento del convertidor para que devuelva null cuando no haya entidades de automóvil para convertir
        when(carConverter.toCar(null)).thenReturn(null);

        // When
        CompletableFuture<List<Car>> completableFuture = carService.saveAll(carRequestsList);
        // Then
        assertEquals(cars.size(), completableFuture.get().size());
    }

    @Test
    void findAll() throws Exception {
        // Given
        // Lista de entidades de automóviles
        List<CarEntity> carEntityList = new ArrayList<>();
        CarEntity carEntity = new CarEntity();
        carEntityList.add(carEntity);

        // Lista de objetos de automóviles esperados
        List<Car> cars = new ArrayList<>();
        cars.add(new Car());

        // Configuramos el comportamiento del repositorio de automóviles para que devuelva la lista de entidades de automóviles que acabamos de crear
        when(carRepository.findAll()).thenReturn(carEntityList);
        // Configuramos el comportamiento del convertidor de automóviles para que convierta la entidad de automóvil en un objeto de automóvil
        when(carConverter.toCar(carEntity)).thenReturn(new Car());

        // When
        // Llamamos al método findAll() del servicio de automóviles
        CompletableFuture<List<Car>> completableFuture = carService.findAll();

        // Then
        // Obtenemos la lista de objetos de automóviles devueltos por el servicio
        List<Car> actualCars = completableFuture.get();
        // Comprobamos que el tamaño de la lista de autos devueltos sea el mismo que el de la lista de autos esperados
        assertEquals(cars.size(), actualCars.size());
        // Comprobamos que los atributos relevantes de los autos devueltos sean iguales a los de los autos esperados
        assertEquals(cars.get(0).getId(), actualCars.get(0).getId());

    }

    @Test
    void findById() throws Exception {
        // Given
        Integer id = 1;
        CarEntity carEntity = new CarEntity();
        // Mocking behavior
        when(carRepository.findById(id)).thenReturn(Optional.of(carEntity));
        when(carConverter.toCar(carEntity)).thenReturn(new Car());
        // When
        Car foundCar = carService.findById(id);
        // Then
        assertEquals(new Car().getId(), foundCar.getId());
    }

    @Test
    void deleteById() throws Exception {
        // Given
        Integer id = 1;
        CarEntity carEntity = new CarEntity();
        // Mocking behavior
        when(carRepository.findById(id)).thenReturn(Optional.of(carEntity));
        // When
        String result = carService.deleteById(id);
        // Then
        assertEquals("Car successfully eliminated", result);
    }

    @Test
    void updateById() throws Exception {
        // Given
        Integer id = 1;
        Car car = new Car();
        CarEntity carEntity = new CarEntity();
        // Mocking behavior
        when(carRepository.findById(id)).thenReturn(Optional.of(carEntity));
        when(carConverter.toCarEntity(car)).thenReturn(carEntity);
        when(carRepository.save(carEntity)).thenReturn(carEntity);
        when(carConverter.toCar(carEntity)).thenReturn(car);
        // When
        Car updatedCar = carService.updateById(id, car);
        // Then
        assertEquals(car, updatedCar);
    }

}