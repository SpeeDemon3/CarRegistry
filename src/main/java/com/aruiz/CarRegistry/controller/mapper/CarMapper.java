package com.aruiz.CarRegistry.controller.mapper;

import com.aruiz.CarRegistry.controller.dto.CarRequest;
import com.aruiz.CarRegistry.controller.dto.CarResponse;
import com.aruiz.CarRegistry.domain.Brand;
import com.aruiz.CarRegistry.domain.Car;
import com.aruiz.CarRegistry.service.BrandService;
import com.aruiz.CarRegistry.service.CarService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * Clase encargada de mapear entre entidades Car y DTOs CarRequest y CarResponse.
 */
@Slf4j
@Component
public class CarMapper {

    @Autowired
    private BrandMapper brandMapper;

    @Autowired
    private CarService carService;

    @Autowired
    private BrandService brandService;


    /**
     * Convierte una entidad Car a un DTO CarResponse.
     *
     * @param entity La entidad Car a convertir.
     * @return CarResponse creado a partir de la entidad Car.
     */
    public CarResponse toCarResponse(Car entity) {
        CarResponse car = new CarResponse();

        // Asigna los atributos del DTO CarResponse con los valores de la entidad Car
        car.setId(entity.getId());
        System.out.println(car.getId());
        // Asigna el objeto BrandResponse al atributo brandResponse del DTO CarResponse
        // a partir de la entidad Car proporcionada
        car.setBrandResponse(brandMapper.toBrandResponse(entity.getBrand()));
        System.out.println("BrandMapper -> : " + brandMapper);
        car.setModel(entity.getModel());
        car.setMilleage(entity.getMilleage());
        car.setPrice(entity.getPrice());
        car.setYear(entity.getYear_car());
        car.setDescription(entity.getDescription_car());
        car.setColour(entity.getColour());
        car.setFuelType(entity.getFuel_type());
        car.setNumDoors(entity.getNum_doors());

        return car;

    }

    /**
     * Convierte un DTO CarRequest a una entidad Car.
     *
     * @param carModel El DTO CarRequest a convertir.
     * @return Entidad Car creada a partir del DTO CarRequest.
     */
    public Car toCarModel(CarRequest carModel) {
        Car car = new Car();
        // Asigna el ID del carModel a la entidad Car
        // car.setId(carModel.getIdBrand());


        System.out.println("BrandMapper -> : " + brandMapper);


        // Si el ID de la marca es mayor que 0, se crea y asigna un objeto Brand a la entidad Car
        if (carModel.getIdBrand() > 0) {
            Brand brand = new Brand();
            brand.setId(carModel.getIdBrand());
            car.setBrand(brand);
        }

        car.setModel(carModel.getModel());
        car.setMilleage(carModel.getMilleage());
        car.setPrice(carModel.getPrice());
        car.setYear_car(carModel.getYear_car());
        car.setDescription_car(carModel.getDescription());
        car.setColour(carModel.getColour());
        car.setFuel_type(carModel.getFuel_type());
        car.setNum_doors(carModel.getNum_doors());

        return car;

    }

    public CarResponse toCarResponse(CarRequest entity) {
        CarResponse car = new CarResponse();

        // Asigna los atributos del DTO CarResponse con los valores de la entidad Car
        car.setId(null);
        System.out.println(car.getId());

        try {
            Optional<Brand> optionalBrand = Optional.ofNullable(brandService.findById(entity.getIdBrand()));

            if (optionalBrand.isPresent()) {
                Brand brand = optionalBrand.get();

                // Asigna el objeto BrandResponse al atributo brandResponse del DTO CarRequest
                // a partir de la entidad Car proporcionada
                car.setBrandResponse(brandMapper.toBrandResponse(brand));
                System.out.println("BrandMapper -> : " + brandMapper);

            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }


        car.setModel(entity.getModel());
        car.setMilleage(entity.getMilleage());
        car.setPrice(entity.getPrice());
        car.setYear(entity.getYear_car());
        car.setDescription(entity.getDescription());
        car.setColour(entity.getColour());
        car.setFuelType(entity.getFuel_type());
        car.setNumDoors(entity.getNum_doors());

        return car;

    }

}
