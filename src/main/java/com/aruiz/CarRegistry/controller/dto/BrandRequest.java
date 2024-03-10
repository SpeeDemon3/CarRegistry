package com.aruiz.CarRegistry.controller.dto;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class BrandRequest {

    private String name_brand;

    private Integer warranty;

    private String country;

}
