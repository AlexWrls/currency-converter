package ru.converter.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.converter.dto.XmlParseDto;
import ru.converter.entity.Currency;
import ru.converter.entity.Rate;
import ru.converter.repository.CurrencyRepo;
import ru.converter.repository.RateRepo;

import java.time.LocalDate;
import java.util.UUID;

/**
 * Сервис сохранения списка актуальных валют
 */
@Service
public class SaveXmlParseService {

    private final CurrencyRepo currencyRepo;
    private final RateRepo rateRepo;
    private final XmlParseService xmlParseService;

    @Autowired
    public SaveXmlParseService(RateRepo rateRepo, CurrencyRepo currencyRepo, XmlParseService xmlParseService) {
        this.rateRepo = rateRepo;
        this.currencyRepo = currencyRepo;
        this.xmlParseService = xmlParseService;
    }

    public void saveXMLParseData() {
        XmlParseDto data = xmlParseService.parseRecourse();
        currencyRepo.saveAll(data.getCurrencies());
        rateRepo.saveAll(data.getRates());

        Currency currency = new Currency();
        currency.setId("R0111");
        currency.setCharCode("RU");
        currency.setName("Российский рубль");
        currency.setNominal(1);
        currency.setValue(1);
        currency.setNumCode("111");

        Rate rate = new Rate();
        rate.setCursDate(xmlParseService.getCursDate());
        rate.setCharCode(currency.getCharCode());
        rate.setValue(currency.getValue());
        rate.setId(UUID.randomUUID().toString());

        currencyRepo.save(currency);
        rateRepo.save(rate);
    }
}
