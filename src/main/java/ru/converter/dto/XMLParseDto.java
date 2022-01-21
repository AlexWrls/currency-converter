package ru.converter.dto;

import lombok.Getter;
import lombok.Setter;
import ru.converter.entity.CurrencyItem;

import java.util.List;

@Getter
@Setter
public class XMLParseDto {
    List<CurrencyItem> currencyItems;
}
