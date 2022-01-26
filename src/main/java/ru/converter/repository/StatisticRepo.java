package ru.converter.repository;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.converter.dto.StatisticDto;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

/**
 * Статистика за период
 */
@Repository
@Slf4j
public class StatisticRepo {
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public StatisticRepo(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<StatisticDto> getStatisticByDateBetween(LocalDate after, LocalDate before) {
        return jdbcTemplate.query("select concat(c.convert_to, ' (', cu.name, ')') as convertTo,\n" +
                "       concat(c.convert_from, ' (',cur.name,')') as convertFrom,\n" +
                "       count(*)                                 as count\n" +
                "from converts as c\n" +
                "         join currencys as cu on cu.char_code like c.convert_to\n" +
                "         join currencys as cur on cur.char_code like c.convert_from\n" +
                "where c.convert_to like cu.char_code\n" +
                "  and c.curs_date >= '" + after + "'\n" +
                "  and c.curs_date <= '" + before + "'\n" +
                "group by concat(c.convert_to, ' (', cu.name, ')'),  concat(c.convert_from, ' (',cur.name,')') ", ((rs, row) -> map(rs)));
    }

    private StatisticDto map(ResultSet res) {
        try {
            StatisticDto statistic = new StatisticDto();
            statistic.setConvertFrom(res.getString("convertFrom"));
            statistic.setConvertTo(res.getString("convertTo"));
            statistic.setCount(res.getInt("count"));
            return statistic;
        } catch (SQLException e) {
            log.error("Ошибка подсчета сатистики по фильтрам");
            throw new RuntimeException(e.getMessage());
        }
    }
}
