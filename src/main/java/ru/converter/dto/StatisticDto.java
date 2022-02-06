package ru.converter.dto;

import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class StatisticDto {
    private LocalDate after;
    private LocalDate before;
    private List<Statistic> statistics;
}
