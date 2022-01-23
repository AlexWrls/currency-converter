package ru.converter.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.converter.dto.ConvertDto;
import ru.converter.dto.XmlParseDto;
import ru.converter.service.ConvertService;
import ru.converter.service.StatisticService;
import ru.converter.service.XmlParseService;

import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.Objects;


@Controller
public class MainController {

    private final XmlParseService parseService;
    private final ConvertService convertService;
    private final StatisticService statisticService;

    @Autowired
    public MainController(XmlParseService parseService, ConvertService convertService, StatisticService statisticService) {
        this.parseService = parseService;
        this.convertService = convertService;
        this.statisticService = statisticService;
    }

    @GetMapping("/")
    public String index(Model model, ConvertDto convert) {
        XmlParseDto data = parseService.parseRecourse();
        model.addAttribute("currency", data.getCurrencies());
        model.addAttribute("rate", data.getRates());
        model.addAttribute("convert", convert);
        if (Objects.isNull(model.getAttribute("statistic"))) {
            model.addAttribute("statistic", statisticService.getStatByDate(LocalDate.now().minusWeeks(1), LocalDate.now()));
        }
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
        after = Objects.isNull(after) ? LocalDate.parse("2000-01-01") : after;
        before = Objects.isNull(before) ? LocalDate.now() : before;
        model.addAttribute("statistic", statisticService.getStatByDate(after, before));
        model.addAttribute("after", after);
        model.addAttribute("before", before);
        return index(model, new ConvertDto());
    }

}
