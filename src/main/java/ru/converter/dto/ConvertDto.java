package ru.converter.dto;

import lombok.Data;

/**
 * Форма для конвертации валют
 */
@Data
public class ConvertDto {
    private String convertTo;
    private String convertFrom;
    private Double valueTo;
    private Double valueFrom;
}
