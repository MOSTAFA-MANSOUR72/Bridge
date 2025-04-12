package com.market.bridge.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity(name = "address")
@Data
public class Address {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String street;

    private String city;

    private String state;

    private String zipCode;

    private String country;


}