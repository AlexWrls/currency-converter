package ru.converter.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.converter.dto.StatisticDto;
import ru.converter.entity.Currency;
import ru.converter.repository.CurrencyRepo;
import ru.converter.repository.StatisticRepo;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Сервис конвертации валют и статистики за период
 */
@Service
public class StatisticService {
    private final CurrencyRepo currencyRepo;
    private final StatisticRepo statisticRepo;

    @Autowired
    public StatisticService(StatisticRepo statisticRepo, CurrencyRepo currencyRepo) {

        this.statisticRepo = statisticRepo;
        this.currencyRepo = currencyRepo;
    }

    /**
     * Получить стаитстику за период
     */
    public List<StatisticDto> getStatByDate(LocalDate after, LocalDate before) {
        return addDecryption(statisticRepo.getStatisticByDateBetween(after, before));
    }

    /**
     * Получить стаитстику за период по поиску конверированной вальты
     */
    public List<StatisticDto> getStatByDateAndConvertTo(LocalDate after, LocalDate before, String convertTo) {
        return addDecryption(statisticRepo.getStatisticByDateBetweenAndConvertTo(after, before, convertTo));
    }

    /**
     * Получить стаитстику за период по поиску конверированной вальты
     */
    public List<StatisticDto> getStatByDateAndConvertFrom(LocalDate after, LocalDate before, String convertFrom) {
        return addDecryption(statisticRepo.getStatisticByDateBetweenAndConvertFrom(after, before, convertFrom));
    }

    /**
     * Получить стаитстику за период по поиску конверированной вальты
     */
    public List<StatisticDto> getStatByDateAndConvertToAndConvertFrom(LocalDate after, LocalDate before, String convertTo, String convertFrom) {
        return addDecryption(statisticRepo.getStatisticByDateBetweenAndConvertToAndConvertFrom(after, before, convertTo, convertFrom));
    }

    /**
     * Установить описание валют
     */
    private List<StatisticDto> addDecryption(List<StatisticDto> statistics) {
        List<Currency> currencies = currencyRepo.findAll();
        for (StatisticDto stat : statistics) {
            String nameCurr = currencies.stream()
                    .filter(curr -> curr.getCharCode().equals(stat.getConvertFrom()))
                    .map(Currency::getName).findFirst().get();
            String convertFrom = String.format("%s (%s)", stat.getConvertFrom(), nameCurr);
            stat.setConvertFrom(convertFrom);
        }
        return statistics;
    }


}
