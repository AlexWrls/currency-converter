package ru.converter.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.converter.dto.StatisticDto;
import ru.converter.entity.Convert;
import ru.converter.entity.Rate;
import ru.converter.repository.ConvertRepo;
import ru.converter.repository.RateRepo;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Сервис конвертации валют
 */
@Service
public class ConvertService {

    private final ConvertRepo convertRepo;
    private final RateRepo rateRepo;
    private final XmlParseService xmlParseService;

    @Autowired
    public ConvertService(ConvertRepo convertRepo, RateRepo rateRepo, XmlParseService xmlParseService) {
        this.convertRepo = convertRepo;
        this.rateRepo = rateRepo;
        this.xmlParseService = xmlParseService;
    }

    /**
     * Выполнение конвертации одной вылюты в другую
     *
     * @param convertTo   CharCode исходной валюты
     * @param convertFrom CharCode преобразованной валюты
     * @param valTo       кол-во исходной валюты
     * @return конверитированая сумма
     */
    public double doConvert(String convertTo, String convertFrom, double valTo) {
        final Rate rateTo = rateRepo.findTopByCharCodeOrderByIdDesc(convertTo);
        final Rate rateFrom = rateRepo.findTopByCharCodeOrderByIdDesc(convertFrom);
        final LocalDate cursDate = xmlParseService.getCursDate();
        double valueFrom;

        Convert convert = new Convert();
        convert.setCursDate(cursDate);
        convert.setConvertTo(convertTo);
        convert.setConvertFrom(convertFrom);
        convert.setValueTo(valTo);

        if (!cursDate.isEqual(rateTo.getCursDate()) || !cursDate.isEqual(rateFrom.getCursDate())) {
            final List<Rate> rates = xmlParseService.parseRecourse().getRates();
            Rate parseRateTo = getRateByCharCode(rates, convertTo);
            Rate parseRateFrom = getRateByCharCode(rates, convertFrom);
            rateRepo.save(parseRateTo);
            rateRepo.save(parseRateFrom);
            valueFrom = calculateConvert(parseRateTo, parseRateFrom, valTo);
        } else {
            valueFrom = calculateConvert(rateTo, rateFrom, valTo);
        }
        convert.setValueFrom(valueFrom);
        convertRepo.save(convert);
        return valueFrom;
    }

    /**
     * Расчет предобразования вылюты
     *
     * @param rateTo   даные исходной валюты
     * @param rateFrom даные преобразованной валюты
     * @param valTo    кол-во исходной валюты
     * @return конверитированая сумма
     */
    private double calculateConvert(Rate rateTo, Rate rateFrom, double valTo) {
        double val = (rateTo.getValue() / rateFrom.getValue()) * valTo;
        BigDecimal result = new BigDecimal(val);
        result = result.setScale(3, RoundingMode.DOWN);
        return Double.parseDouble(String.valueOf(result));
    }

    /**
     * Получение данных по CharCode валюты
     *
     * @return данные валюты из списка rates по фильтру charCode
     */
    private Rate getRateByCharCode(List<Rate> rates, String charCode) {
        return rates.stream()
                .filter(r -> r.getCharCode().equals(charCode))
                .findFirst().orElse(null);
    }

}
