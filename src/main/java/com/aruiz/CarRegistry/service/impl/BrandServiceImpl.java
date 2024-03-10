package com.aruiz.CarRegistry.service.impl;


import com.aruiz.CarRegistry.controller.dto.BrandRequest;
import com.aruiz.CarRegistry.domain.Brand;
import com.aruiz.CarRegistry.entity.BrandEntity;
import com.aruiz.CarRegistry.repository.BrandRepository;
import com.aruiz.CarRegistry.service.BrandService;
import com.aruiz.CarRegistry.service.converter.BrandConverter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

@Slf4j
@Service
public class BrandServiceImpl implements BrandService {

    @Autowired
    private BrandRepository brandRepository;

    @Autowired
    private BrandConverter brandConverter;


    /**
     * Método para guardar una nueva marca
     * @param brandRequest
     * @return
     * @throws Exception
     */
    @Override
    public Brand save(Brand brandRequest) throws Exception {
        // Convierte el objeto de dominio en una entidad y lo guarda en el repositorio
        BrandEntity brandEntity = brandConverter.toBrandEntity(brandRequest);

        return brandConverter.toBrand(brandRepository.save(brandEntity));

    }


    /**
     * Método para guardar una lista de marcas de modo asíncrono
     * @param brandRequestList
     * @return
     * @throws Exception
     */
    @Override
    @Async
    public CompletableFuture<List<Brand>> saveAll(List<BrandRequest> brandRequestList) throws Exception {
        // Tiempo en el que se hace la llamada
        long startTime = System.currentTimeMillis();

        List<Brand> brands =new ArrayList<>();

        for (BrandRequest brand : brandRequestList) {
            // Convierte la solicitud de marca en una entidad de marca y la guarda en el repositorio
            BrandEntity brandEntity = brandConverter.toBrandEntity(brand);

            // Guarda la entidad de marca en el repositorio, convierte la entidad guardada en un objeto de marca y lo agrega a la lista
            Brand brandNew = brandConverter.toBrand(brandRepository.save(brandEntity));

            brands.add(brandNew);

        }

        // Tiempo en el que finaliza la llamada
        long endTime = System.currentTimeMillis();
        log.info("Total process time: " + (endTime - startTime) + "ms.");

        // Devuelve la lista de marcas guardadas
        return CompletableFuture.completedFuture(brands);
    }


    /**
     * Método para obtener todas las marcas
     * @return
     * @throws Exception
     */
    @Override
    @Async
    public CompletableFuture<List<Brand>> findAll() throws Exception {

        // Tiempo en el que se hace la llamada
        long startTime = System.currentTimeMillis();

        // Obtiene la lista de entidades de marca desde el repositorio
        List<BrandEntity> brandEntityList = brandRepository.findAll();

        List<Brand> brands = new ArrayList<>();
        // Itera sobre la lista de entidades de marcas y las convierte en objetos de dominio
        brandEntityList.forEach(brand -> brands.add(brandConverter.toBrand(brand)));

        // Tiempo en el que finaliza la llamada
        long endTime = System.currentTimeMillis();
        // Registra el tiempo total que tomó el proceso
        log.info("Total process time: " + (endTime - startTime) + "ms.");

        // Devuelve la lista de marcas encontradas
        return CompletableFuture.completedFuture(brands);

    }

    /**
     * Método para buscar una marca por ID
     * @param id
     * @return
     * @throws Exception
     */
    @Override
    public Brand findById(Integer id) throws Exception {
        // Busca la entidad de marca correspondiente al ID proporcionado en el repositorio
        Optional<BrandEntity> optionalBrand = brandRepository.findById(id);

        if (optionalBrand.isPresent()) {
            log.info("optionalBrand -> " + optionalBrand);
            // Convierte la entidad en un objeto de dominio y lo devuelve
            return brandConverter.toBrand(optionalBrand.get());
        }

        return null;

    }

    /**
     * Método para eliminar una marca por ID
     * @param id
     * @return
     * @throws Exception
     */
    @Override
    public boolean delete(Integer id) throws Exception {
        // Busca la entidad de marca correspondiente al ID proporcionado en el repositorio
        Optional<BrandEntity> optionalBrand = brandRepository.findById(id);

        // Verifica si la entidad de marca está presente
        if (optionalBrand.isPresent()) {
            // Elimina la entidad de marca del repositorio
            brandRepository.deleteById(id);

            return true;
        }

        return false;

    }

    /**
     * Método para actualizar una marca por ID
     * @param id
     * @param brandRequest
     * @return
     * @throws Exception
     */
    @Override
    public Brand updateBrand(Integer id, Brand brandRequest) throws Exception {
        log.info("Updating brand with id {}" + id);

        // Busca la marca por ID en el repositorio
        Optional<BrandEntity> optionalBrand = brandRepository.findById(id);

        if (optionalBrand.isPresent()) {
            // Convierte el objeto de dominio en una entidad y lo guarda en el repositorio
            BrandEntity brandEntity = brandConverter.toBrandEntity(brandRequest);
            // Establece el ID proporcionado al objeto de entidad antes de guardarlo
            brandEntity.setId(id);

            return brandConverter.toBrand(brandRepository.save(brandEntity));

        }

        return null;

    }


}
