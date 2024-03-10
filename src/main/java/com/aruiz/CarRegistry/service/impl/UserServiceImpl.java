package com.aruiz.CarRegistry.service.impl;

import com.aruiz.CarRegistry.entity.UserEntity;
import com.aruiz.CarRegistry.repository.UserRepository;
import com.aruiz.CarRegistry.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Base64;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;


    @Override
    public UserEntity save(UserEntity newUser) throws Exception {
        return userRepository.save(newUser);
    }

    /**
     * Carga un usuario por su nombre de usuario (en este caso, el correo electrónico).
     *
     * @param emailUser El correo electrónico del usuario a cargar.
     * @return Los detalles del usuario cargado.
     * @throws UsernameNotFoundException Si no se encuentra ningún usuario con el correo electrónico proporcionado.
     */
    @Override
    public UserDetails loadUserByUsername(String emailUser) throws UsernameNotFoundException {
        // Buscamos el usuario en la base de datos por su correo electrónico
        return userRepository.findByEmail(emailUser)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + emailUser));

    }

    /**
     * Método para guardar la imagen de un usuario en la base de datos.
     *
     * @param id El ID del usuario al que se asociará la imagen.
     * @param imgFile El archivo de imagen a guardar.
     * @throws IOException Si ocurre un error de entrada/salida al leer el archivo de imagen.
     */
    @Override
    public void addUserImage(Long id, MultipartFile imgFile) throws IOException {
        // Obtener el usuario con el ID proporcionado
        UserEntity userEntity = userRepository.findById(id).orElseThrow(RuntimeException::new);

        log.info("Saving user image....");
        // Codificar la imagen en base64 y establecerla en el atributo "img" del usuario
        userEntity.setImg(Base64.getEncoder().encodeToString(imgFile.getBytes()));
        // Guardar el usuario actualizado en la base de datos
        userRepository.save(userEntity);
    }

    /**
     * Método para obtener la imagen de un usuario desde la base de datos.
     *
     * @param id El ID del usuario del que se desea obtener la imagen.
     * @return Un arreglo de bytes que representa la imagen del usuario.
     */
    @Override
    public byte[] getUserImage(Long id) {
        // Obtener el usuario con el ID proporcionado
         UserEntity userEntity = userRepository.findById(id).orElseThrow(RuntimeException::new);

        // Decodificar la imagen en base64 y devolverla un arreglo de bytes
        return Base64.getDecoder().decode(userEntity.getImg());

    }


}


