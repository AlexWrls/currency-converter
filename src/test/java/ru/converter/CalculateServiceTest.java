package ru.converter;

import org.junit.jupiter.api.Test;
import org.springframework.util.Assert;
import ru.converter.entity.Rate;
import ru.converter.repository.RateRepo;
import ru.converter.service.ConvertService;

import java.util.List;

public class CalculateServiceTest {
    RateRepo rateRepo;
    ConvertService convertService;

    @Test
    public void test() {
        List<Rate> rates = rateRepo.findAll();
        for (Rate rate : rates) {
            for (Rate rate1 : rates) {
                double v = convertService.doConvert(rate.getCharCode(), rate1.getCharCode(), 1d);
                Assert.isTrue(rate.getValue() == v);
            }
        }
    }
}
