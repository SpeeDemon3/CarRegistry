package com.aruiz.CarRegistry.controller.mapper;

import com.aruiz.CarRegistry.controller.dto.BrandRequest;
import com.aruiz.CarRegistry.controller.dto.BrandResponse;
import com.aruiz.CarRegistry.domain.Brand;
import org.springframework.stereotype.Component;

/**
 * Clase encargada de mapear y convertir objetos relacionados con la entidad Brand.
 */
@Component
public class BrandMapper {
    /**
     * Convierte una entidad Brand a la respuesta de la marca (BrandResponse).
     *
     * @param brand La entidad Brand a convertir.
     * @return La respuesta de la marca (BrandResponse) obtenida después de la conversión.
     */
    public BrandResponse toBrandResponse(Brand brand) {
        BrandResponse brandResponse = new BrandResponse();

        brandResponse.setId(brand.getId());
        brandResponse.setName_brand(brand.getName_brand());
        brandResponse.setWarranty(brand.getWarranty());
        brandResponse.setCountry(brand.getCountry());

        return brandResponse;

    }

    /**
     * Convierte una solicitud de marca (BrandRequest) a la entidad Brand.
     *
     * @param brandRequest La solicitud de marca a convertir.
     * @return La entidad Brand obtenida después de la conversión.
     */
    public Brand toBrandModel(BrandRequest brandRequest) {
        Brand brand = new Brand();

        brand.setName_brand(brandRequest.getName_brand());
        brand.setWarranty(brandRequest.getWarranty());
        brand.setCountry(brandRequest.getCountry());

        return brand;

    }

    public BrandResponse toBrandResponse(BrandRequest brandRequest) {
        BrandResponse brandResponse = new BrandResponse();

        brandResponse.setId(null);
        brandResponse.setName_brand(brandRequest.getName_brand());
        brandResponse.setWarranty(brandRequest.getWarranty());
        brandResponse.setCountry(brandRequest.getCountry());

        return brandResponse;

    }


}
