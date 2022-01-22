package ru.converter.dto;

import lombok.Getter;
import lombok.Setter;
import ru.converter.entity.Currency;
import ru.converter.entity.Rate;

import java.util.List;

@Getter
@Setter
public class XmlParseDto {
   private List<Currency> currencies;
   private List<Rate> rates;
}
