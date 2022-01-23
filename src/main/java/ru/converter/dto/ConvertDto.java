package ru.converter.dto;

import lombok.Data;

@Data
public class ConvertDto {
    private String convertTo;
    private String convertFrom;
    private double valueTo;
    private Double valueFrom;
}
