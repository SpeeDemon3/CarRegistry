package com.aruiz.CarRegistry.service.converter;

import com.aruiz.CarRegistry.domain.Brand;
import com.aruiz.CarRegistry.domain.Car;
import com.aruiz.CarRegistry.entity.BrandEntity;
import com.aruiz.CarRegistry.entity.CarEntity;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CarConverterTest {

    @InjectMocks
    private CarConverter carConverter;

    @Mock
    private BrandConverter brandConverter;

    @Test
    void toCar() {
        // Give
        BrandEntity brandEntity = new BrandEntity();
        brandEntity.setId(1);

        CarEntity carEntity = new CarEntity();
        carEntity.setId(3);
        carEntity.setBrand(brandEntity);
        carEntity.setModel("Seat");
        carEntity.setMilleage(89000);
        carEntity.setPrice(41000.0);
        carEntity.setYear_car(2017);
        carEntity.setColour("white");
        carEntity.setFuel_type("Gasoline");
        carEntity.setNum_doors(5);

        Brand brand = new Brand();
        brand.setId(1);

        Car car = new Car();
        car.setId(3);
        car.setBrand(brand);
        car.setModel("Seat");
        car.setMilleage(89000);
        car.setPrice(41000.0);
        car.setYear_car(2017);
        car.setColour("white");
        car.setFuel_type("Gasoline");
        car.setNum_doors(5);

        // When
        when(brandConverter.toBrand(brandEntity)).thenReturn(brand);

        // Then
        Car result = carConverter.toCar(carEntity);
        assertEquals(result.getId(), car.getId());
        assertEquals(result.getBrand(), car.getBrand());
        assertEquals(result.getModel(), car.getModel());
        assertEquals(result.getMilleage(), car.getMilleage());
        assertEquals(result.getPrice(), car.getPrice());
        assertEquals(result.getYear_car(), car.getYear_car());
        assertEquals(result.getColour(), car.getColour());
        assertEquals(result.getFuel_type(), car.getFuel_type());
        assertEquals(result.getNum_doors(), car.getNum_doors());

    }

    @Test
    void toCarEntity() {

        // Give
        Brand brand = new Brand();
        brand.setId(1);

        Car car = new Car();
        car.setId(3);
        car.setBrand(brand);
        car.setModel("Seat");
        car.setMilleage(89000);
        car.setPrice(41000.0);
        car.setYear_car(2017);
        car.setColour("white");
        car.setFuel_type("Gasoline");
        car.setNum_doors(5);

        BrandEntity brandEntity = new BrandEntity();
        brandEntity.setId(1);

        CarEntity carEntity = new CarEntity();
        carEntity.setId(3);
        carEntity.setBrand(brandEntity);
        carEntity.setModel("Seat");
        carEntity.setMilleage(89000);
        carEntity.setPrice(41000.0);
        carEntity.setYear_car(2017);
        carEntity.setColour("white");
        carEntity.setFuel_type("Gasoline");
        carEntity.setNum_doors(5);

        // When
        when(brandConverter.toBrandEntity(brand)).thenReturn(brandEntity);

        // Then
        CarEntity result = carConverter.toCarEntity(car);
        assertEquals(result.getId(), carEntity.getId());
        assertEquals(result.getBrand(), carEntity.getBrand());
        assertEquals(result.getModel(), carEntity.getModel());
        assertEquals(result.getMilleage(), carEntity.getMilleage());
        assertEquals(result.getPrice(), carEntity.getPrice());
        assertEquals(result.getYear_car(), carEntity.getYear_car());
        assertEquals(result.getDescription_car(), carEntity.getDescription_car());
        assertEquals(result.getColour(), carEntity.getColour());
        assertEquals(result.getFuel_type(), carEntity.getFuel_type());
        assertEquals(result.getNum_doors(), carEntity.getNum_doors());

    }

}