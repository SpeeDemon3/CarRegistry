package com.aruiz.CarRegistry.config;

import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;

@EnableAsync
public class AsyncConfig {

    @Bean(name = "taskExecutor")
    public Executor taskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();

        // Numero de hilos que se podran ejecutar
        executor.setCorePoolSize(10);

        // Maximo de hilos que se ejecutan a la vez
        executor.setMaxPoolSize(5);

        // Capacidad que tendra la cola
        executor.setQueueCapacity(50);

        // Nombre del hilo
        executor.setThreadNamePrefix("ThreadCarRegistry-");

        // Inicializa
        executor.initialize();

        return executor;
    }

}
