package com.aruiz.CarRegistry.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "car")
public class CarEntity {
    @Id
    @GeneratedValue
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "id_brand", nullable = false)
    private BrandEntity brand;

    @Column(name = "model")
    private String model;

    @Column(name = "milleage")
    private Integer milleage;

    @Column(name = "price")
    private Double price;

    @Column(name = "year_car")
    private Integer year_car;

    @Column(name = "description_car")
    private String description_car;

    @Column(name = "colour")
    private String colour;

    @Column(name = "fuel_type")
    private String fuel_type;

    @Column(name = "num_doors")
    private Integer num_doors;


}
