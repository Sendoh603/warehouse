package com.cpan228.warehouse.dto;

import lombok.Data;

@Data
public class DistributionCentre {
    private Long id;
    private String name;
    private double latitude;
    private double longitude;
    private String address;
}