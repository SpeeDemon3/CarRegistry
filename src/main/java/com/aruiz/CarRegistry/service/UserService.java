package com.aruiz.CarRegistry.service;

import com.aruiz.CarRegistry.entity.UserEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface UserService extends  UserDetailsService {

    UserEntity save(UserEntity newUser) throws Exception;

    UserDetails loadUserByUsername(String emailUser) throws UsernameNotFoundException;


    void addUserImage(Long id, MultipartFile imgFile) throws IOException;

    byte[] getUserImage(Long id);
}
