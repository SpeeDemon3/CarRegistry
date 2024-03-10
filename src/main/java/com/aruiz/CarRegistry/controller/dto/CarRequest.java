package com.aruiz.CarRegistry.controller.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class CarRequest {

    @JsonProperty("id_brand")
    private int idBrand;
    private String model;
    private Integer milleage;
    private Double price;
    private Integer year_car;
    @JsonProperty("description_car")
    private String description;
    @JsonProperty("colour")
    private String colour;
    @JsonProperty("fuel_type")
    private String fuel_type;
    @JsonProperty("num_doors")
    private Integer num_doors;

}
