package ru.converter.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.converter.dto.ConvertDto;
import ru.converter.entity.Currency;
import ru.converter.entity.Rate;
import ru.converter.service.ConvertService;
import ru.converter.service.StatisticService;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Контроллер взаимодействия с view
 */
@Controller
public class MainController {

    private final ConvertService convertService;
    private final StatisticService statisticService;

    @Autowired
    public MainController(ConvertService convertService, StatisticService statisticService) {
        this.convertService = convertService;
        this.statisticService = statisticService;
    }

    @GetMapping("/")
    public String index(Model model, ConvertDto convert) {
        model.addAttribute("currency", convertService.getAllCurrency());
        model.addAttribute("convert", convert);
        statisticService.setStatisticModel(model);
        return "index";
    }

    @PostMapping("/calculate")
    public String calculate(Model model, ConvertDto convert) {
        final double valFrom = convertService.doConvert(convert.getConvertTo(), convert.getConvertFrom(), convert.getValueTo());
        convert.setValueFrom(valFrom);
        return index(model, convert);
    }

    @GetMapping("/statistics")
    public String statistics(
            @RequestParam(defaultValue = "") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate after,
            @RequestParam(defaultValue = "") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate before,
            Model model) {
        model.addAttribute("statistic", statisticService.getStatByDate(after, before));
        model.addAttribute("after", after);
        model.addAttribute("before", before);
        return index(model, new ConvertDto());
    }

    @GetMapping("/graph")
    public String graph(Model model, @RequestParam(defaultValue = "AUD") String charCode) {
        final List<Double> rates = convertService.getAllRateByCharCode(charCode).stream()
                .map(Rate::getValue)
                .collect(Collectors.toList());
        Currency currency = convertService.getCurrencyByCharCode(charCode);
        model.addAttribute("graphCurrency",String.format("%s (%s)",currency.getCharCode(),currency.getName()));
        model.addAttribute("rates", rates);
        return index(model, new ConvertDto());
    }
}
