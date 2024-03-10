package com.aruiz.CarRegistry.service.impl;

import com.aruiz.CarRegistry.controller.dto.CarRequest;
import com.aruiz.CarRegistry.domain.Car;
import com.aruiz.CarRegistry.entity.BrandEntity;
import com.aruiz.CarRegistry.entity.CarEntity;
import com.aruiz.CarRegistry.repository.BrandRepository;
import com.aruiz.CarRegistry.repository.CarRepository;
import com.aruiz.CarRegistry.service.CarService;
import com.aruiz.CarRegistry.service.converter.CarConverter;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

@Slf4j
@Service
public class CarServiceImpl implements CarService {

    @Autowired
    private CarRepository carRepository;

    @Autowired
    private BrandRepository brandRepository;


    @Autowired
    private CarConverter carConverter;


    private final String[] HEADERS ={"colour", "description_car", "fuel_type", "milleage", "model",
    "num_doors", "price", "year_car", "id_brand"};

    /**
     * Método para guardar un nuevo coche
     * @param carRequest
     * @return
     * @throws Exception
     */
    @Override
    public Car save(Car carRequest) throws Exception {

        log.info("Saving car: " + carRequest);

        // Convierte el objeto de solicitud de automóvil a una entidad de automóvil
        CarEntity carEntity = carConverter.toCarEntity(carRequest);
        log.info("Converted to CarEntity: " + carEntity);

        // Guarda la entidad de automóvil en el repositorio y obtiene la entidad guardada
        CarEntity savedCarEntity = carRepository.save(carEntity);
        log.info("Saved CarEntity: " + savedCarEntity);

        // Convierte la entidad guardada de automóvil a un objeto de automóvil y lo devuelve
        return carConverter.toCar(savedCarEntity);

    }

    /**
     * Método para guardar una lista de coches de forma asincrona
     *
     * @param carRequestsList
     * @return
     * @throws Exception
     */
    @Override
    @Async
    public CompletableFuture<List<Car>> saveAll(List<CarRequest> carRequestsList) throws Exception {
        // Tiempo en el que se hace la llamada
        long startTime = System.currentTimeMillis();

        log.info("Saving cars...");

        List<Car> cars = new ArrayList<>();

        for (CarRequest carRequest: carRequestsList) {
            // Convierte la solicitud de automóvil en una entidad de automóvil
            CarEntity carEntity = carConverter.toCarEntity(carRequest);
            log.info("Converted to CarEntity: " + carEntity);

            // Guarda la entidad de automóvil en el repositorio y obtiene la entidad guardada
            CarEntity savedCarEntity = carRepository.save(carEntity);
            log.info("Saved CarEntity: " + savedCarEntity);

            // Convierte la entidad guardada de automóvil a un objeto de automóvil y lo guarda en la lista
            cars.add(carConverter.toCar(savedCarEntity));
        }

        // Tiempo en el que finaliza la llamada
        long endTime = System.currentTimeMillis();
        // Tiempo en el que finaliza la llamada
        log.info("Total process time: " + (endTime - startTime) + "ms.");

        // Devuelve la lista de objetos de automóviles guardados
        return CompletableFuture.completedFuture(cars);
    }

    /**
     * Método para obtener todos los automoviles de manera asíncrona
     * @return
     * @throws Exception
     */
    @Override
    @Async
    public CompletableFuture<List<Car>> findAll() throws Exception {
        // Tiempo en el que se hace la llamada
        long startTime = System.currentTimeMillis();

        // Recupera todas las entidades CarEntity de la base de datos
        List<CarEntity> carEntityList = carRepository.findAll();

        // Inicializa una lista para almacenar objetos de automóviles convertidos
        List<Car> cars = new ArrayList<>();
        // Itera sobre la lista de entidades y convierte cada entidad a un objeto de automóvil
        carEntityList.forEach(car -> cars.add(carConverter.toCar(car)));

        // Tiempo en el que finaliza la llamada
        long endTime = System.currentTimeMillis();
        // Registra el tiempo total que tomó la operación
        log.info("Total time: " + (endTime - startTime) + "ms.");

        // Devuelve la lista de objetos de automóviles convertidos
        return CompletableFuture.completedFuture(cars);

    }

    /**
     * Busca un objeto Car por su ID.
     *
     * @param id El ID del objeto Car que se va a buscar.
     * @return El objeto Car si se encuentra, o null si no se encuentra.
     * @throws Exception Si ocurre algún error durante la operación.
     */
    @Override
    public Car findById(Integer id) throws Exception {
        // Intenta encontrar una entidad CarEntity por su ID en la base de datos
        Optional<CarEntity> carEntityOptional = carRepository.findById(id);

        // Verifica si la entidad CarEntity está presente
        if (carEntityOptional.isPresent()) {
            // Convierte la entidad CarEntity a un objeto Car y lo devuelve
            return carConverter.toCar(carEntityOptional.get());
        }
        // Retorna null si no se encuentra ninguna entidad correspondiente al ID proporcionado
        return null;
    }


    /**
     * Elimina un objeto Car por su ID.
     *
     * @param id El ID del objeto Car que se va a eliminar.
     * @return Un mensaje indicando si la eliminación fue exitosa o si no se encontró el objeto Car con el ID proporcionado.
     * @throws Exception Si ocurre algún error durante la operación.
     */
    @Override
    public String deleteById(Integer id) throws Exception {
        // Intenta encontrar una entidad CarEntity por su ID en la base de datos
        Optional<CarEntity> optionalCarEntity = carRepository.findById(id);

        // Verifica si la entidad CarEntity está presente en la base de datos
        if (optionalCarEntity.isPresent()) {
            // Si está presente, elimina la entidad con el ID proporcionado
            carRepository.deleteById(id);
            return "Car successfully eliminated";
        } else {
            return "Car with ID -> " + id + " not found";
        }

    }

    /**
     * Actualiza un objeto Car por su ID.
     *
     * @param id         El ID del objeto Car que se va a actualizar.
     * @param carRequest El objeto Car con los datos actualizados.
     * @return El objeto Car actualizado, o null si el ID no se encuentra.
     * @throws Exception Si ocurre algún error durante la operación.
     */
    @Override
    public Car updateById(Integer id, Car carRequest) throws Exception {
        log.info("Updating car with id {}" + id);
        // Intenta encontrar una entidad CarEntity por su ID en la base de datos
        Optional<CarEntity> carEntityOptional = carRepository.findById(id);

        // Verifica si la entidad CarEntity está presente
        if (carEntityOptional.isPresent()) {
            // Convierte el objeto Car a una entidad CarEntity
            CarEntity carEntity = carConverter.toCarEntity(carRequest);

            // Establece el ID proporcionado al objeto CarEntity
            carEntity.setId(id);

            // Guarda la entidad CarEntity actualizada en la base de datos y la convierte a un objeto Car
            return carConverter.toCar(carRepository.save(carEntity));

        }
        // Retorna null si el ID proporcionado no se encuentra en la base de datos
        return null;
    }

    /**
     * Genera un contenido CSV que contiene la información de todos los coches en la base de datos.
     * Cada línea del CSV representa un coche y sus detalles asociados, incluyendo los detalles de la marca.
     *
     * @return Una cadena que contiene el contenido CSV de todos los coches.
     */
    @Override
    public String carsDownloadCsv() {

        // Lista de todos los coches desde en base de datos
        List<CarEntity> carEntityList = carRepository.findAll();

        // StringBuilder para almacenar el contenido CSV
        StringBuilder csvContent = new StringBuilder();

        int count = 1;

        for (String header : HEADERS) {
            csvContent.append(header).append(",");

            if (count == HEADERS.length) {
                csvContent.append(header).append("\n");
            }

            count++;

        }


        for (CarEntity carEntity: carEntityList) {
            // Agregar los datos del coche al contenido CSV, separados por comas
            csvContent
                    .append(carEntity.getId()).append(",")
                    .append(carEntity.getColour()).append(",")
                    .append(carEntity.getDescription_car()).append(",")
                    .append(carEntity.getFuel_type()).append(",")
                    .append(carEntity.getMilleage()).append(",")
                    .append(carEntity.getModel()).append(",")
                    .append(carEntity.getNum_doors()).append(",")
                    .append(carEntity.getPrice()).append(",")
                    .append(carEntity.getYear_car()).append(",")
                    // Obtener y agregar los detalles de la marca del coche
                    .append(carEntity.getBrand().getId()).append(",")
                    .append(carEntity.getBrand().getCountry()).append(",")
                    .append(carEntity.getBrand().getName_brand()).append(",")
                    .append(carEntity.getBrand().getWarranty()).append("\n");
        }

        return csvContent.toString();
    }

    /**
     * Método para cargar datos de coches desde un archivo CSV.
     *
     * @param file Archivo CSV que contiene los datos de los coches.
     * @return Lista de entidades de coches creadas a partir de los datos del archivo CSV.
     * @throws RuntimeException Si ocurre un error durante la carga de los coches desde el archivo CSV.
     */
    @Override
    public List<CarEntity> uploadCars(MultipartFile file) {

        List<CarEntity> carEntityList = new ArrayList<>();


        try (BufferedReader fileReader = new BufferedReader(new InputStreamReader(file.getInputStream(), "UTF-8"));
            CSVParser csvParser = new CSVParser(fileReader,
                    CSVFormat.DEFAULT.withFirstRecordAsHeader().withIgnoreHeaderCase().withTrim());
        ) {

            Iterable<CSVRecord> csvRecords = csvParser.getRecords();

            // Iterar sobre cada registro en el archivo CSV
            for (CSVRecord record : csvRecords) {
                CarEntity carEntity = new CarEntity();

                // Asignar valores de los campos del registro al objeto CarEntity
                carEntity.setColour(record.get(HEADERS[0]));
                carEntity.setDescription_car(record.get(HEADERS[1]));
                carEntity.setFuel_type(record.get(HEADERS[2]));
                carEntity.setMilleage(Integer.valueOf(record.get(HEADERS[3])));
                carEntity.setModel(record.get(HEADERS[4]));
                carEntity.setNum_doors(Integer.valueOf(record.get(HEADERS[5])));
                carEntity.setPrice(Double.valueOf(record.get(HEADERS[6])));
                carEntity.setYear_car(Integer.valueOf(record.get(HEADERS[7])));

                // Obtener el ID de la marca del registro CSV
                String brandId = record.get(HEADERS[8]);

                // Buscar la marca correspondiente en la base de datos
                BrandEntity brand = brandRepository.findById(Integer.parseInt(brandId)).orElseThrow(() -> new RuntimeException(
                        "Brand not found for ID: " + brandId));

                // Asignar la marca al objeto CarEntity
                carEntity.setBrand(brand);

                // Agregar el objeto CarEntity a la lista
                carEntityList.add(carEntity);

            }

            // Guardar todas las entidades CarEntity en la base de datos
            carEntityList = carRepository.saveAll(carEntityList);

        } catch (Exception e) {
            // Manejar cualquier excepción que ocurra durante el proceso de carga
            log.error("Failed to load cars");
            log.info("Filename: {}", file.getOriginalFilename());
            throw new RuntimeException("Failed to load cars");
        }

        // Devolver la lista de entidades CarEntity creadas
        return carEntityList;
    }



}
