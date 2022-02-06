package ru.converter.dto;

import lombok.Data;
import ru.converter.entity.Rate;

import java.time.LocalDate;
import java.util.List;

@Data
public class ChartDto {
    private String charCode;
    private List<LocalDate> cursDate;
    private List<Double> values;
}
