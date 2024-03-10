package com.aruiz.CarRegistry.service.converter;

import com.aruiz.CarRegistry.controller.dto.BrandRequest;
import com.aruiz.CarRegistry.domain.Brand;
import com.aruiz.CarRegistry.entity.BrandEntity;
import org.springframework.stereotype.Component;

@Component
public class BrandConverter {

    /**
     * Método para convertir una entidad BrandEntity a un objeto Brand
     * @param entity BrandEntity
     * @return objeto Brand
     */
    public Brand toBrand(BrandEntity entity) {
        Brand brand = new Brand();

        // Asignar los atributos de la entidad a la instancia de Brand
        brand.setId(entity.getId());
        brand.setName_brand(entity.getName_brand());
        brand.setWarranty(entity.getWarranty());
        brand.setCountry(entity.getCountry());

        // Devolver la instancia de Brand
        return brand;

    }

    /**
     * Método para convertir un objeto Brand a una entidad BrandEntity
     * @param brand
     * @return
     */
    public BrandEntity toBrandEntity(Brand brand) {
        // Crear una nueva instancia de BrandEntity
        BrandEntity brandEntity = new BrandEntity();

        // Asignar los atributos del objeto Brand al BrandEntity
        brandEntity.setId(brand.getId());
        brandEntity.setName_brand(brand.getName_brand());
        brandEntity.setWarranty(brand.getWarranty());
        brandEntity.setCountry(brand.getCountry());

        // Devolver la instancia de BrandEntity creada y configurada
        return brandEntity;

    }

    /**
     * Método para convertir un objeto BrandRequest a una entidad BrandEntity
      * @param brandRequest
     * @return
     */
    public BrandEntity toBrandEntity(BrandRequest brandRequest) {
        // Crear una nueva instancia de BrandEntity
        BrandEntity brandEntity = new BrandEntity();

        // Asignar los atributos del objeto Brand al BrandEntity
        brandEntity.setId(null);
        brandEntity.setName_brand(brandRequest.getName_brand());
        brandEntity.setWarranty(brandRequest.getWarranty());
        brandEntity.setCountry(brandRequest.getCountry());

        // Devolver la instancia de BrandEntity creada y configurada
        return brandEntity;

    }

}
