package ru.converter.dto;

import lombok.Getter;
import lombok.Setter;

/**
 * Класс для формирования статистики за период
 */
@Getter
@Setter
public class StatisticDto {
    private String convertTo;
    private String convertFrom;
    private Integer count;
}
