package com.aruiz.CarRegistry.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BrandResponse {
    private Integer id;

    private String name_brand;

    private Integer warranty;

    private String country;


}
