package com.aruiz.CarRegistry.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Brand {

    private Integer id;

    private String name_brand;

    private Integer warranty;

    private String country;


}
