package com.aruiz.CarRegistry.service.converter;

import com.aruiz.CarRegistry.controller.dto.CarRequest;
import com.aruiz.CarRegistry.domain.Brand;
import com.aruiz.CarRegistry.domain.Car;
import com.aruiz.CarRegistry.entity.CarEntity;
import com.aruiz.CarRegistry.service.BrandService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class CarConverter {

    @Autowired
    private BrandService brandService;

    @Autowired
    private BrandConverter brandConverter;

    /**
     * Convierte una entidad CarEntity a un objeto de dominio de Car
     * @param entity CarEntity
     * @return  objeto de dominio de Car
     */
    public Car toCar(CarEntity entity) {
        Car car = new Car();
        // Configura las propiedades del objeto de dominio usando la entidad CarEntity
        car.setId(entity.getId());
        // Configura la marca del coche en el objeto de dominio utilizando el convertidor de marca
        car.setBrand(brandConverter.toBrand(entity.getBrand()));
        car.setModel(entity.getModel());
        car.setMilleage(entity.getMilleage());
        car.setPrice(entity.getPrice());
        car.setYear_car(entity.getYear_car());
        car.setDescription_car(entity.getDescription_car());
        car.setColour(entity.getColour());
        car.setFuel_type(entity.getFuel_type());
        car.setNum_doors(entity.getNum_doors());

        return car;

    }

    /**
     * Convierte un objeto de dominio Car a una entidad CarEntity
     * @param car objeto de dominio
     * @return objeto CarEntity
     */
    public CarEntity toCarEntity(Car car) {
        CarEntity carEntity = new CarEntity();

        // Configura las propiedades de la entidad usando el objeto de dominio
        carEntity.setId(car.getId());
        carEntity.setModel(car.getModel());
        carEntity.setMilleage(car.getMilleage());
        carEntity.setPrice(car.getPrice());
        carEntity.setYear_car(car.getYear_car());
        carEntity.setDescription_car(car.getDescription_car());
        carEntity.setColour(car.getColour());
        carEntity.setFuel_type(car.getFuel_type());
        carEntity.setNum_doors(car.getNum_doors());

        // Usa brandConverter para convertir y asignar la marca
        carEntity.setBrand(brandConverter.toBrandEntity(car.getBrand()));

        return carEntity;

    }

    /**
     * Convierte un objeto de dominio Car a una entidad CarEntity
     * @param car objeto dto
     * @return objeto CarEntity
     */
    public CarEntity toCarEntity(CarRequest car) {
        CarEntity carEntity = new CarEntity();

        // Configura las propiedades de la entidad usando el objeto de dominio
        carEntity.setId(null);
        carEntity.setModel(car.getModel());
        carEntity.setMilleage(car.getMilleage());
        carEntity.setPrice(car.getPrice());
        carEntity.setYear_car(car.getYear_car());
        carEntity.setDescription_car(car.getDescription());
        carEntity.setColour(car.getColour());
        carEntity.setFuel_type(car.getFuel_type());
        carEntity.setNum_doors(car.getNum_doors());

        try {
            Optional<Brand> optionalBrandEntity = Optional.ofNullable(brandService.findById(car.getIdBrand()));

            if (optionalBrandEntity.isPresent()) {
                Brand brand = optionalBrandEntity.get();
                // Usa brandConverter para convertir y asignar la marca
                carEntity.setBrand(brandConverter.toBrandEntity(brand));
            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return carEntity;

    }



}
