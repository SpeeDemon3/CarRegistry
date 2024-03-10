package com.aruiz.CarRegistry.controller;

import com.aruiz.CarRegistry.controller.dto.LoginRequest;
import com.aruiz.CarRegistry.controller.dto.SingUpRequest;
import com.aruiz.CarRegistry.service.UserService;
import com.aruiz.CarRegistry.service.impl.AuthenticationService;
import com.aruiz.CarRegistry.service.impl.UserServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Controlador para la gestión de usuarios.
 */
@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
@Slf4j
public class UserController {

    private final AuthenticationService authenticationService;

    private final UserServiceImpl userService;

    /**
     * Endpoint para el registro de un nuevo usuario.
     *
     * @param request Datos de registro del usuario.
     * @return Respuesta con el token JWT generado para el nuevo usuario.
     */
    @PostMapping("/signup")
    public ResponseEntity<?> singup(@RequestBody SingUpRequest request) {
        try {
            log.info("request {}", request);
            return ResponseEntity.ok(authenticationService.signup(request));
        } catch (Exception e) {
            log.info("request {}", request);
            log.error("{}", e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    /**
     * Endpoint para el inicio de sesión de un usuario existente.
     *
     * @param request Datos de inicio de sesión del usuario.
     * @return Respuesta con el token JWT generado para el usuario que ha iniciado sesión.
     */
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        return ResponseEntity.ok(authenticationService.login(request));
    }

    /**
     * Endpoint para cargar un archivo de imagen.
     *
     * @param file El archivo de imagen a cargar.
     * @return ResponseEntity que indica el resultado de la operación.
     */
    @PostMapping(value = "/uploadFile", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> uploadImg(@RequestParam(value = "file")MultipartFile file) {

        // Si el archivo está vacío, devuelve una respuesta de error
        if(file.isEmpty()) {
            log.error("File it's empty.");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

        log.info("The file name is {}", file.getOriginalFilename());
        log.info("The file size is {}", file.getSize());

        return ResponseEntity.ok("Image succesfully uploaded");
    }

    /**
     * Endpoint para descargar un archivo del sistema de archivos.
     * En este caso, se descarga un archivo de imagen PNG.
     *
     * @return ResponseEntity que contiene el archivo a descargar.
     * @throws IOException Si ocurre un error al leer el archivo.
     */
    @GetMapping(value = "/downloadFile")
    public ResponseEntity<?> downloadFile() throws IOException {
        // Especificar el tipo de contenido del archivo (imagen PNG en este caso)
        MediaType contentType = MediaType.IMAGE_PNG;

        // Abrir un flujo de entrada para leer el archivo del sistema de archivos
        InputStream inputStream = new FileInputStream("src/main/resources/seat.png");

        // Devolver una respuesta con el archivo como cuerpo de la respuesta
        // y establecer el tipo de contenido adecuado
        return ResponseEntity.ok()
                .contentType(contentType)
                .body(new InputStreamResource(inputStream));

    }

    /**
     * Endpoint para agregar una imagen a un usuario en base de datos.
     *
     * @param id El ID del usuario al que se asociará la imagen.
     * @param imageFile El archivo de imagen a agregar.
     * @return ResponseEntity que indica el resultado de la operación.
     */
    @PreAuthorize("hasAnyRole('CLIENT', 'VENDOR')")
    @PostMapping("/addImgUser/{id}/add")
    public ResponseEntity<String> addImgUser(@PathVariable Long id, @RequestParam("imageFile") MultipartFile imageFile) {

        try {
            // Verificar si el archivo de imagen no tiene extensión .png
            if (!imageFile.getOriginalFilename().contains(".png")) {
                log.error("The image must be in PNG format.");
                ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
            }

            // Llamar al servicio para agregar la imagen al usuario
            userService.addUserImage(id, imageFile);
            log.info("Image name: {}", imageFile.getOriginalFilename());
            log.info("Image size: {}", imageFile.getSize());

            // Devolver una respuesta exitosa
            return ResponseEntity.ok("Image succesfully saved.");

        } catch (Exception e) {
            // En caso de error, registrar información de depuración y devolver una respuesta de error
            log.error("The image could not be added to the user.");
            log.info(imageFile.getOriginalFilename());
            log.info(String.valueOf(imageFile.getSize()));
            log.info(imageFile.getContentType());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

    }

    /**
     * Endpoint para descargar la imagen de un usuario a partir de su ID.
     *
     * @param id El ID del usuario cuya imagen se desea obtener.
     * @return ResponseEntity con los bytes de la imagen y los encabezados HTTP apropiados.
     */
    @PreAuthorize("hasAnyRole('CLIENT', 'VENDOR')")
    @GetMapping("/userImg/{id}")
    public ResponseEntity<byte[]> getUserImg(@PathVariable Long id) {

        try {
            // Obtener los bytes de la imagen del usuario utilizando el servicio
            byte[] imageBytes = userService.getUserImage(id);

            // Configurar los encabezados HTTP para indicar que la respuesta contiene una imagen PNG
            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.setContentType(MediaType.IMAGE_PNG);

            // Devolver una respuesta con los bytes de la imagen, los encabezados HTTP y un estado OK
            return new ResponseEntity<>(imageBytes, httpHeaders, HttpStatus.OK);

        } catch (Exception e) {
            // En caso de error, devolver una respuesta de error con un estado BAD_REQUEST
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

    }


}
