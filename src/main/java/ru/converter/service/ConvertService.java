package ru.converter.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.converter.entity.Convert;
import ru.converter.entity.Rate;
import ru.converter.repository.ConvertRepo;
import ru.converter.repository.RateRepo;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.List;

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

    public void doConvert(String convertTo, String convertFrom, double valTo) {
        final Rate rateTo = rateRepo.findTopByCharCodeOrderByIdDesc(convertTo);
        final Rate rateFrom = rateRepo.findTopByCharCodeOrderByIdDesc(convertFrom);
        final LocalDate cursDate = xmlParseService.getCursDate();

        if (!cursDate.isEqual(rateTo.getCursDate()) && !cursDate.isEqual(rateFrom.getCursDate())) {
            final List<Rate> rates = xmlParseService.parseRecourse().getRates();
            Rate parseRateTo = getRateByCharCode(rates, convertTo);
            Rate parseRateFrom = getRateByCharCode(rates, convertFrom);
            rateRepo.save(parseRateTo);
            rateRepo.save(parseRateFrom);
            calculateConvert(parseRateTo, parseRateFrom, valTo);
        } else {
            calculateConvert(rateTo, rateFrom, valTo);
        }
    }

    private void calculateConvert(Rate rateTo, Rate rateFrom, double valTo) {
        double val = (rateTo.getValue() / rateFrom.getValue()) * valTo;
        BigDecimal result = new BigDecimal(val);
        result = result.setScale(3, RoundingMode.DOWN);
        final double valFrom = Double.parseDouble(String.valueOf(result));

        Convert convert = new Convert();
        convert.setConvertTo(rateFrom.getCharCode());
        convert.setConvertFrom(rateFrom.getCharCode());
        convert.setValueTo(valTo);
        convert.setValueFrom(valFrom);
        convertRepo.save(convert);
    }

    private Rate getRateByCharCode(List<Rate> rates, String charCode) {
        return rates.stream()
                .filter(r -> r.getCharCode().equals(charCode))
                .findFirst().orElse(null);
    }
}
