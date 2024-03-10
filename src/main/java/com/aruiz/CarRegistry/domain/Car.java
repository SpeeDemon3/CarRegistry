package com.aruiz.CarRegistry.domain;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Car {

    private Integer id;

    private Brand brand;

    private String model;

    private Integer milleage;

    private Double price;

    private Integer year_car;

    private String description_car;

    private String colour;

    private String fuel_type;

    private Integer num_doors;



}
