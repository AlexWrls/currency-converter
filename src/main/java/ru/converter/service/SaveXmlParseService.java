package ru.converter.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.converter.dto.XmlParseDto;
import ru.converter.repository.CurrencyRepo;
import ru.converter.repository.RateRepo;

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
    }
}
