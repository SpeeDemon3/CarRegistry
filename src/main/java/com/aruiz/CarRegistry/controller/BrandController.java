package com.aruiz.CarRegistry.controller;

import com.aruiz.CarRegistry.controller.dto.BrandRequest;
import com.aruiz.CarRegistry.controller.mapper.BrandMapper;
import com.aruiz.CarRegistry.service.BrandService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * Controlador que gestiona las operaciones relacionadas con las marcas de automóviles.
 */
@Slf4j
@RestController
@RequestMapping("/api/brand")
public class BrandController {

    @Autowired
    private BrandService brandService;

    @Autowired
    private BrandMapper brandMapper;

    /**
     * Endpoint para agregar una nueva marca.
     *
     * @param brandRequest Objeto de solicitud que representa la marca a agregar.
     * @return ResponseEntity con el objeto BrandRequest agregado en caso de éxito.
     */
    @PostMapping("/addBrand")
    @PreAuthorize("hasRole('VENDOR')")
    public ResponseEntity<?> addBrand(@RequestBody BrandRequest brandRequest) {
        try {
            log.info("successfully created brand");
            // Guarda la marca utilizando el servicio
            // Convirtiendo a model
            brandService.save(brandMapper.toBrandModel(brandRequest));
            System.out.println(brandRequest);
            return ResponseEntity.ok().body(brandRequest);
        } catch (Exception e) {
            log.error("Server Error -> " + e);
            return ResponseEntity.internalServerError().build();
        }

    }

    /**
     * Endpoint para agregar una nueva lista de marcas.
     *
     * @param brandRequestList Objeto de solicitud que representa la lista de marcas a agregar.
     * @return
     */
    @PostMapping("/addBrands")
    @PreAuthorize("hasRole('VENDOR')")
    public CompletableFuture<?> addBrands(@RequestBody List<BrandRequest> brandRequestList) throws Exception {
        // Llama al método saveAll() del servicio de marcas para guardar la lista de marcas
        return brandService.saveAll(brandRequestList)
                // Utiliza thenApply() para transformar el resultado del CompletableFuture
                .thenApply(brands -> {
                    // Devuelve un ResponseEntity con estado 200 (OK) si la operación de guardado es exitosa
                    return ResponseEntity.ok().body(brands);
                })
                // Maneja cualquier excepción que ocurra durante el procesamiento
                .exceptionally(ex -> {
                    // Lanza una nueva RuntimeException con un mensaje descriptivo y estado 500 (Internal Server Error)
                    throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error saving brands", ex);
                });


    }

    /**
     * Endpoint para obtener una marca por su ID.
     *
     * @param id ID de la marca a recuperar.
     * @return ResponseEntity con la marca recuperada en caso de éxito.
     */
    @GetMapping("/getBrand/{id}")
    @PreAuthorize("hasRole('CLIENT')")
    public ResponseEntity<?> getBrandById(@PathVariable Integer id) {
        try {
            log.info("Looking for brand with ID -> " + id);
            return ResponseEntity.ok().body(brandService.findById(id));
        } catch (Exception e) {
            log.warn("The brand with ID " + id + " is not found in the database.");
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Endpoint para obtener todas las marcas de forma asíncrona.
     *
     * @return
     */
    @GetMapping("/getBrands")
    @PreAuthorize("hasRole('CLIENT')")
    public CompletableFuture<?> getBrands() throws Exception {
        // Llama al método findAll() del servicio de marcas para obtener una lista de marcas
        return brandService.findAll()
                // Utiliza thenApply() para transformar el resultado del CompletableFuture
                .thenApply(brands -> {
                    // Verifica si la lista de marcas está vacía
                    if (brands.isEmpty()) {
                        // Si no hay marcas, devuelve un ResponseEntity con estado 404 (Not Found)
                        return ResponseEntity.notFound().build();
                    } else {
                        // Si hay marcas, devuelve un ResponseEntity con estado 200 (OK) y la lista de marcas
                        return ResponseEntity.ok().body(brands);
                    }
                })
                // Maneja cualquier excepción que ocurra durante el procesamiento
                .exceptionally(ex -> {
                    // Lanza una nueva RuntimeException con un mensaje descriptivo si ocurre una excepción
                    throw new RuntimeException("Error retrieving brands.");
                });
    }

    /**
     * Endpoint para eliminar una marca por su ID.
     *
     * @param id ID de la marca a eliminar.
     * @return ResponseEntity con un mensaje indicando el resultado de la operación.
     */
    @DeleteMapping("/deleteBrand/{id}")
    @PreAuthorize("hasRole('VENDOR')")
    public ResponseEntity<?> deleteBrandById(@PathVariable Integer id) {
        try {
            log.info("Brand with ID -> " + id +  " successfully deleted");
            return ResponseEntity.ok().body(brandService.delete(id));
        } catch (Exception e) {
            log.info("Brand with ID -> " + id +  " not found in the database.");
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Endpoint para actualizar una marca por su ID.
     *
     * @param id ID de la marca a actualizar.
     * @param brandRequest Objeto de solicitud que representa la marca actualizada.
     * @return ResponseEntity con la marca actualizada en caso de éxito.
     */
    @PutMapping("/updateBrand/{id}")
    @PreAuthorize("hasRole('VENDOR')")
    public ResponseEntity<?> updateBrandByID(@PathVariable Integer id, @RequestBody BrandRequest brandRequest) {
        try {
            log.info("Brand with ID -> " + id + " updated successfully");
            return ResponseEntity.ok().body(brandService.updateBrand(id, brandMapper.toBrandModel(brandRequest)));
        } catch (Exception e) {
            log.info("Brand with ID -> " + id +  " not found in the database.");
            return ResponseEntity.notFound().build();
        }
    }

}
