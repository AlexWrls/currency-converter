package ru.converter.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.converter.dto.StatisticDto;
import ru.converter.entity.Convert;
import ru.converter.repository.ConvertRepo;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Сервис конвертации валют и статистики за период
 */
@Service
public class StatisticService {
    private final ConvertRepo convertRepo;

    @Autowired
    public StatisticService(ConvertRepo convertRepo) {
        this.convertRepo = convertRepo;
    }


    /**
     * Получить стаитстику за период
     *
     * @param after  полсле даты
     * @param before до даты
     * @return статистика за пириод
     */
    public List<StatisticDto> getStatByDate(LocalDate after, LocalDate before) {
        List<Convert> converts = convertRepo.findAllByCursDateBetween(after, before);
        Map<Convert, Integer> map = new HashMap<>();
        List<StatisticDto> statistic = new ArrayList<>();
        for (Convert convert1 : converts) {
            int count = 0;
            for (Convert convert2 : converts) {
                if (convert1.getConvertTo().equals(convert2.getConvertTo())
                        && convert1.getConvertFrom().equals(convert2.getConvertFrom())) {
                    count++;
                }
            }
            if (!isRecord(convert1, map)) {
                map.put(convert1, count);
            }
        }
        map.forEach((k, v) -> statistic.add(StatisticDto.builder()
                .convertTo(k.getConvertTo())
                .convertFrom(k.getConvertFrom())
                .count(v).build()));
        return statistic;
    }

    /**
     * Проверка является ли запись уникальной
     */
    private boolean isRecord(Convert convert, Map<Convert, Integer> map) {
        if (map.isEmpty()) return false;
        for (Map.Entry<Convert, Integer> entry : map.entrySet()) {
            if (entry.getKey().getConvertFrom().equals(convert.getConvertFrom()) && entry.getKey().getConvertTo().equals(convert.getConvertTo())) {
                return true;
            }
        }
        return false;
    }
}
