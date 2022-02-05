package ru.converter.dto;

import lombok.Getter;
import lombok.Setter;

/**
 * Формирование статистики за период
 */

public interface Statistic {
     String getConvertTo();
     String getConvertFrom();
     Integer getCount();
}
