package com.aruiz.CarRegistry.controller.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class CarResponse {

    @JsonProperty("id")
    private Integer id;
    @JsonProperty("id_brand")
    private BrandResponse brandResponse;
    @JsonProperty("model")
    private String model;
    @JsonProperty("milleage")
    private Integer milleage;
    @JsonProperty("price")
    private Double price;
    @JsonProperty("year_car")
    private Integer year;
    @JsonProperty("description_car")
    private String description;
    @JsonProperty("colour")
    private String colour;
    @JsonProperty("fuel_type")
    private String fuelType;
    @JsonProperty("num_doors")
    private Integer numDoors;

}
