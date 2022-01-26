package ru.converter.dto;

import lombok.Builder;
import lombok.Getter;
import ru.converter.entity.Currency;
import ru.converter.entity.Rate;

import java.util.List;

/**
 * Класс для заполнеия при разборе курса валют с сайта ЦБРФ
 */
@Getter
@Builder
public class XmlParseDto {

    private final List<Currency> currencies;
    private final List<Rate> rates;

}
