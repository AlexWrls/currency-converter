package ru.converter.dto;

import lombok.Builder;
import lombok.Getter;

/**
 * Класс для формирования статистики за период
 */
@Getter
@Builder
public class StatisticDto {
    private String convertTo;
    private String convertFrom;
    private Integer count;
}
