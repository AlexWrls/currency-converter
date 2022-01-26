package ru.converter.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.converter.dto.XmlParseDto;
import ru.converter.entity.Currency;
import ru.converter.entity.Rate;
import ru.converter.repository.CurrencyRepo;
import ru.converter.repository.RateRepo;

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

    private static final String ID = "R0111";
    private static final String CHAR_CODE = "RUB";
    private static final String NAME = "Российский рубль";

    public void saveXMLParseData() {
        XmlParseDto data = xmlParseService.parseRecourse();
        currencyRepo.saveAll(data.getCurrencies());
        rateRepo.saveAll(data.getRates());
    }

    public void addRu() {
        Currency currency = new Currency();
        currency.setId(ID);
        currency.setCharCode(CHAR_CODE);
        currency.setName(NAME);
        currency.setNominal(1);
        currency.setValue(1d);
        currency.setNumCode("1");

        Rate rate = new Rate();
        rate.setCursDate(xmlParseService.getCursDate());
        rate.setCharCode(CHAR_CODE);
        rate.setValue(1d);
        rate.setId(UUID.randomUUID().toString());

        currencyRepo.save(currency);
        rateRepo.save(rate);
    }
}
