package ru.converter.service;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.converter.dto.Statistic;
import ru.converter.dto.StatisticDto;
import ru.converter.entity.Convert;
import ru.converter.entity.Currency;
import ru.converter.entity.Rate;
import ru.converter.repository.ConvertRepo;
import ru.converter.repository.CurrencyRepo;
import ru.converter.repository.RateRepo;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * Сервис конвертации валют
 */
@Service
@AllArgsConstructor
public class ConvertService {

    private final ConvertRepo convertRepo;
    private final RateRepo rateRepo;
    private final XmlParseService xmlParseService;
    private final CurrencyRepo currencyRepo;


    /**
     * Выполнение конвертации одной вылюты в другую
     *
     * @param convertTo   CharCode исходной валюты
     * @param convertFrom CharCode преобразованной валюты
     * @param valTo       кол-во исходной валюты
     * @return конверитированая сумма
     */
    public double doConvert(String convertTo, String convertFrom, double valTo) {
        LocalDate curseDate = LocalDate.now();
        Rate rateTo = getRateByCurseDateAndCharCode(curseDate, convertTo);
        Rate rateFrom = getRateByCurseDateAndCharCode(curseDate, convertFrom);

        //Если в БД отсутствует расценки валют на сегодняшний день
        if (rateTo == null || rateFrom == null) {
            //Получить актуальную дату для списока валют с сайта ЦБРФ
            LocalDate actualDate = xmlParseService.getCursDate();
            //Получить последнюю дату расценок валют из БД
            LocalDate lastBaseDate = rateRepo.findTopByOrderByCursDateDesc().getCursDate();
            //Если даты отличаются, в таблицу расценок БД добавляются актуальные расценки с сайта ЦБРФ
            if (!actualDate.equals(lastBaseDate)) {
                List<Rate> rates = xmlParseService.parseRecourse().getRates();
                rateTo = getRateByCharCode(rates, convertTo);
                rateFrom = getRateByCharCode(rates, convertFrom);
                rateRepo.saveAll(rates);
            }
            //Если 2 полученные даты совпадают, курсы валют считываются из БД
            else {
                rateTo = getRateByCurseDateAndCharCode(actualDate, convertTo);
                rateFrom = getRateByCurseDateAndCharCode(actualDate, convertFrom);
            }
        }
        //Вычисление резульатата конвертирования
        double valueFrom = valTo * (rateTo.getValue() / rateFrom.getValue());
        valueFrom = Math.round(valueFrom * 1000.0) / 1000.0;

        Convert convert = new Convert();
        convert.setConvertTo(convertTo);
        convert.setConvertFrom(convertFrom);
        convert.setValueTo(valTo);
        convert.setValueFrom(valueFrom);
        convert.setCursDate(curseDate);

        convertRepo.save(convert);
        return valueFrom;
    }

    /**
     * Список всех валют
     */
    public List<Currency> getAllCurrency() {
        return currencyRepo.findAll();
    }

    /**
     * Список всех расценок по charCode;
     */
    public List<Rate> getAllRateByCharCode(String charCode) {
        return rateRepo.findAllByCharCodeOrderByCursDate(charCode);
    }

    /**
     * Получит статистику за период;
     */
    public List<Statistic> getStatisticByDateBetween(StatisticDto statisticDto) {
        statisticDto.setAfter(Objects.isNull(statisticDto.getAfter())?LocalDate.now().minusWeeks(1):statisticDto.getAfter());
        statisticDto.setBefore(Objects.isNull(statisticDto.getBefore())?LocalDate.now():statisticDto.getBefore());
        return convertRepo.getStatistic(statisticDto.getAfter(), statisticDto.getBefore());
    }

    /**
     * Получить вылюту по charCode;
     */
    public Currency getCurrencyByCharCode(String charCode) {
        return currencyRepo.findByCharCode(charCode);
    }

    /**
     * Получить расценки валют из БД на сегодня
     */
    private Rate getRateByCurseDateAndCharCode(LocalDate curseDate, String charCode) {
        Rate rate = null;
        Optional<Rate> rateToOpt = rateRepo.findByCursDateAndCharCode(curseDate, charCode);
        if (rateToOpt.isPresent()) {
            rate = rateToOpt.get();
        } else if (charCode.equals("RUB")) {
            rate = rateRepo.findByCharCode("RUB");
        }
        return rate;
    }

    /**
     * Получить расценки по charCode
     */
    private Rate getRateByCharCode(List<Rate> rates, String charCode) {
        if (charCode.equals("RUB")) {
            return rateRepo.findByCharCode("RUB");
        } else {
            return rates.stream().filter(rate -> rate.getCharCode().equals(charCode))
                    .findFirst()
                    .orElseThrow(RuntimeException::new);
        }
    }

}
