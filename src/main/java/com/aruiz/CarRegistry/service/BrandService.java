package com.aruiz.CarRegistry.service;


import com.aruiz.CarRegistry.controller.dto.BrandRequest;
import com.aruiz.CarRegistry.domain.Brand;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@Service
public interface BrandService {

    public Brand save(Brand brandRequest) throws Exception;

    public CompletableFuture<List<Brand>> saveAll(List<BrandRequest> brandRequestList) throws Exception;
    public CompletableFuture<List<Brand>> findAll() throws Exception;

    public Brand findById(Integer id) throws Exception;

    public boolean delete(Integer id) throws Exception;

    public Brand updateBrand(Integer id, Brand brandRequest) throws Exception;

}
