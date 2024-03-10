package com.aruiz.CarRegistry.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "brand")
public class BrandEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name="name_brand")
    private String name_brand;

    @Column(name="warranty")
    private Integer warranty;

    @Column(name="country")
    private String country;

    @OneToMany(mappedBy = "brand")
    private List<CarEntity> car;

}
