package com.aruiz.CarRegistry.controller;

import com.aruiz.CarRegistry.controller.dto.CarRequest;
import com.aruiz.CarRegistry.controller.mapper.CarMapper;
import com.aruiz.CarRegistry.service.CarService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * Controlador REST que gestiona las operaciones relacionadas con la entidad Car.
 */
@Slf4j
@RestController
@RequestMapping("/api/car")
public class CarController {

    @Autowired
    private CarService carService;

    @Autowired
    private CarMapper carMapper;


    /**
     * Endpoint para agregar un nuevo automóvil a la base de datos.
     *
     * @param carRequest La solicitud de creación de un automóvil.
     * @return ResponseEntity que indica el éxito o el error de la operación.
     */
    @PostMapping("/addCar")
    @PreAuthorize("hasRole('VENDOR')")
    public ResponseEntity<?> addCar(@RequestBody CarRequest carRequest) {
        try {
            log.info("Car -> " + carRequest.getIdBrand());
            carService.save(carMapper.toCarModel(carRequest));
            return ResponseEntity.ok().body(carRequest);
        } catch (Exception e) {
            log.error("Error -> " + e);
            log.info("" + carRequest);
            return ResponseEntity.internalServerError().build();
        }
    }

    /**
     * Endpoint para añadir una lista de autos de forma asíncrona
     * @param carRequestList
     * @return
     */
    @PostMapping("/addCars")
    @PreAuthorize("hasRole('VENDOR')")
    public CompletableFuture<?> addCards(@RequestBody List<CarRequest> carRequestList) throws Exception {
        // Llama al método saveAll() del servicio de coches para guardar la lista de coches
        return carService.saveAll(carRequestList)
                // Utiliza thenApply() para transformar el resultado del CompletableFuture
                .thenApply(cars -> {
                    // Devuelve un ResponseEntity con estado 200 (OK) si la operación de guardado es exitosa
                    return ResponseEntity.ok().body(cars);
                })
                // Maneja cualquier excepción que ocurra durante el procesamiento
                .exceptionally(ex -> {
                    // Lanza una nueva RuntimeException con un mensaje descriptivo y estado 500 (Internal Server Error)
                    throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error saving cars", ex);
                });


    }

    /**
     * Endpoint para recuperar todos los autos de la base de datos de forma asíncrona
     * @return
     * @throws Exception
     */
    @GetMapping("/getCars")
    @PreAuthorize("hasRole('CLIENT')")
    public CompletableFuture<?> getCards() throws Exception {
        // Llama al método findAll() del servicio de coches para obtener una lista de coches
        return carService.findAll().
                // Utiliza thenApply() para transformar el resultado del CompletableFuture
                thenApply(cars -> {
                    // Verifica si la lista de coches está vacía
                    if (cars.isEmpty()) {
                        // Si no hay coches, devuelve un ResponseEntity con estado 404 (Not Found)
                        return ResponseEntity.notFound().build();
                    } else {
                        // Si hay coches, devuelve un ResponseEntity con estado 200 (OK) y la lista de coches
                        return ResponseEntity.ok(cars);
                    }
                    // Maneja cualquier excepción que ocurra durante el procesamiento
                }).exceptionally(ex -> {
                    // Lanza una nueva RuntimeException con un mensaje descriptivo si ocurre una excepción
                    throw new RuntimeException("Error retrieving cars.");
                });

    }

    /**
     * Endpoint para obtener un automóvil por su ID.
     *
     * @param id El ID del automóvil a buscar.
     * @return ResponseEntity con el automóvil si se encuentra, o notFound si no.
     */
    @GetMapping("/getCar/{id}")
    @PreAuthorize("hasRole('CLIENT')")
    public ResponseEntity<?> getCardById(@PathVariable Integer id) {
        try {
            return ResponseEntity.ok().body(carService.findById(id));
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Endpoint para eliminar un automóvil por su ID.
     *
     * @param id El ID del automóvil a eliminar.
     * @return ResponseEntity indicando el éxito o el error de la operación.
     */
    @DeleteMapping("/deleteCar/{id}")
    @PreAuthorize("hasRole('VENDOR')")
    public ResponseEntity<?> deleteCarByID(@PathVariable Integer id) {
        try {
            return ResponseEntity.ok().body(carService.deleteById(id));
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Endpoint para actualizar un automóvil por su ID.
     *
     * @param id          El ID del automóvil a actualizar.
     * @param carRequest  La solicitud de actualización del automóvil.
     * @return ResponseEntity con el automóvil actualizado si se encuentra, o notFound si no.
     */
    @PutMapping("/updateCar/{id}")
    @PreAuthorize("hasRole('VENDOR')")
    public  ResponseEntity<?> updateCarByID(@PathVariable Integer id, @RequestBody CarRequest carRequest) {
        try {
            return ResponseEntity.ok().body(carService.updateById(id, carMapper.toCarModel(carRequest)));
        }catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Endpoint para cargar un archivo CSV que contiene datos de coches y procesarlo para
     * almacenar los registros en la base de datos.
     *
     * @param file El archivo CSV a cargar, enviado como parte de la solicitud multipart.
     * @return ResponseEntity que indica el estado de la carga del archivo.
     */
    @PreAuthorize("hasRole('VENDOR')")
    @PostMapping(value = "/uploadCSV", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> uploadCSV(@RequestParam(value = "file")MultipartFile file) {

        // Verificar si el archivo está vacío
        if (file.isEmpty()) {
            log.error("The file it's empty.");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

        // Verificar si el archivo tiene la extensión .csv
        if (file.getOriginalFilename().contains(".csv")) {
            // Procesar el archivo CSV para cargar los datos de los coches en la base de datos
            carService.uploadCars(file);

            // Mostrar información sobre el archivo cargado
            log.info("Filename: {}", file.getOriginalFilename());
            log.info("File size: {}", file.getSize());

            // Devolver una respuesta exitosa
            return ResponseEntity.ok("File successfully uploaded.");
        }

        // Devolver una respuesta indicando que el archivo no es un CSV
        log.error("The file it's not a CSV");
        return ResponseEntity.ok("The file it's not a CSV");

    }


    /**
     * Endpoint que maneja la descarga de un archivo CSV con información de coches.
     * Genera el contenido del archivo CSV a partir de la información de los coches en la base de datos
     * y devuelve una respuesta HTTP con el contenido del archivo para que el cliente lo descargue.
     *
     * @return ResponseEntity con el contenido del archivo CSV de coches para su descarga.
     * @throws IOException si ocurre un error al generar el archivo CSV.
     */
    @PreAuthorize("hasAnyRole('CLIENT', 'VENDOR')")
    @GetMapping(value = "/downloadFileCars")
    public ResponseEntity<?> downloadFileCars() throws IOException {
        // Configuración de los encabezados de la respuesta HTTP para indicar que se va a enviar un archivo para descargar
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        headers.setContentDispositionFormData("attachment", "cars.csv");

        // Se genera el contenido del archivo CSV a partir de la información de los coches en la base de datos
        byte[] csvBytes = carService.carsDownloadCsv().getBytes();

        // Se crea una respuesta HTTP con el contenido del archivo CSV para que el cliente lo descargue
        return new ResponseEntity<>(csvBytes, headers, HttpStatus.OK);
    }

}
