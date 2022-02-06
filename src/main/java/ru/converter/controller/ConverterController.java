package ru.converter.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;
import ru.converter.dto.ChartDto;
import ru.converter.dto.ConvertDto;
import ru.converter.dto.Statistic;
import ru.converter.dto.StatisticDto;
import ru.converter.entity.Currency;
import ru.converter.entity.Rate;
import ru.converter.service.ConvertService;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@AllArgsConstructor
public class ConverterController {

    private final ConvertService convertService;

    @GetMapping("/")
    public ModelAndView index() {
        ModelAndView view = new ModelAndView();
        List<Currency> currency = convertService.getAllCurrency();
        view.addObject("currency", currency);
        view.setViewName("index");
        return view;
    }

    @PostMapping("/calculate")
    public ResponseEntity<?> calculate(@RequestBody ConvertDto convertDto) {
        double valueFrom = convertService.doConvert(convertDto.getConvertTo(), convertDto.getConvertFrom(), convertDto.getValueTo());
        convertDto.setValueFrom(valueFrom);
        return ResponseEntity.ok(convertDto);
    }

    @PostMapping("/statistics")
    public ResponseEntity<?> statistics(@RequestBody StatisticDto statisticDto) {
        List<Statistic> statistic = convertService.getStatisticByDateBetween(statisticDto);
        statisticDto.setStatistics(statistic);
        return ResponseEntity.ok(statisticDto);
    }

    @PostMapping("/chart")
    public ResponseEntity<?> chart(@RequestBody ChartDto chartDto) {
        List<Rate> rates = convertService.getAllRateByCharCode(chartDto.getCharCode());
        chartDto.setCursDate(rates.stream().map(Rate::getCursDate).collect(Collectors.toList()));
        chartDto.setValues(rates.stream().map(Rate::getValue).collect(Collectors.toList()));
        return ResponseEntity.ok(chartDto);
    }

}
