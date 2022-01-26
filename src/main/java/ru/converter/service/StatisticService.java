package ru.converter.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import ru.converter.dto.StatisticDto;
import ru.converter.repository.StatisticRepo;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

/**
 * Сервис конвертации валют и статистики за период
 */
@Service
public class StatisticService {

    private final StatisticRepo statisticRepo;

    @Autowired
    public StatisticService(StatisticRepo statisticRepo) {
        this.statisticRepo = statisticRepo;
    }

    /**
     * Получить стаитстику по фильтрам
     */
    public List<StatisticDto> getStatByDate(LocalDate after, LocalDate before) {
        after = Objects.isNull(after) ? LocalDate.now().minusWeeks(1) : after;
        before = Objects.isNull(before) ? LocalDate.now().plusDays(1) : before;
        return statisticRepo.getStatisticByDateBetween(after, before);
    }

    public void setStatisticModel(Model model) {
        if (Objects.isNull(model.getAttribute("statistic"))) {
            LocalDate after = LocalDate.now().minusWeeks(1);
            LocalDate before = LocalDate.now().plusDays(1);
            model.addAttribute("after", after);
            model.addAttribute("before", before);
            model.addAttribute("statistic", statisticRepo.getStatisticByDateBetween(after, before));
        }
    }
}
