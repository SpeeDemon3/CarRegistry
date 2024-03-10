package com.aruiz.CarRegistry.service;

import com.aruiz.CarRegistry.controller.dto.CarRequest;
import com.aruiz.CarRegistry.domain.Car;
import com.aruiz.CarRegistry.entity.CarEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@Service
public interface CarService {

    Car save(Car carRequest) throws Exception;

    CompletableFuture<List<Car>> saveAll(List<CarRequest> carRequestsList) throws Exception;

    CompletableFuture<List<Car>>findAll() throws Exception;

    Car findById(Integer id) throws Exception;

    String deleteById(Integer id) throws Exception;

    Car updateById(Integer id, Car carRequest) throws Exception;

    List<CarEntity> uploadCars(MultipartFile file);

    String carsDownloadCsv();

}
