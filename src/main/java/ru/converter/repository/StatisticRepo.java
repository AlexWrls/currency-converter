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
        return jdbcTemplate.query("select concat(c.convert_to,' (',cu.name,')') as convertTo, convert_from as convertFrom, count(*) as count from converts as c\n" +
                "join currencys as cu on cu.char_code like c.convert_to or cu.char_code like c.convert_from\n" +
                "where c.convert_to like cu.char_code and c.curs_date >= '" + after + "' and c.curs_date  <= '" + before + "' \n" +
                "group by concat(c.convert_to,' (',cu.name,')'),convert_from", ((rs, row) -> map(rs)));
    }

    public List<StatisticDto> getStatisticByDateBetweenAndConvertTo(LocalDate after, LocalDate before, String convertTo) {
        return jdbcTemplate.query("select concat(c.convert_to,' (',cu.name,')') as convertTo, convert_from as convertFrom, count(*) as count from converts as c\n" +
                "join currencys as cu on cu.char_code like c.convert_to \n" +
                "where c.convert_to like '" + convertTo + "' and c.curs_date >= '" + after + "' and c.curs_date  <= '" + before + "' \n" +
                "group by concat(c.convert_to,' (',cu.name,')'),convert_from", ((rs, row) -> map(rs)));
    }

    public List<StatisticDto> getStatisticByDateBetweenAndConvertFrom(LocalDate after, LocalDate before, String convertFrom) {
        return jdbcTemplate.query("select concat(c.convert_to,' (',cu.name,')') as convertTo, concat(convert_from) convertFrom, count(*) as count from converts as c\n" +
                "join currencys as cu on  cu.char_code like c.convert_from\n" +
                "where c.convert_from  like '" + convertFrom + "' and c.curs_date >= '" + after + "' and c.curs_date  <= '" + before + "' \n" +
                "group by concat(c.convert_to,' (',cu.name,')'), concat(convert_from)", ((rs, row) -> map(rs)));
    }

    public List<StatisticDto> getStatisticByDateBetweenAndConvertToAndConvertFrom(LocalDate after, LocalDate before, String convertTo, String convertFrom) {
        return jdbcTemplate.query("select concat(c.convert_to,' (',cu.name,')') as convertTo, concat(convert_from) convertFrom, count(*) as count from converts as c\n" +
                "join currencys as cu on  cu.char_code like c.convert_from\n" +
                "where c.convert_from  like '" + convertFrom + "' and c.convert_to like '" + convertTo + "' " +
                "and c.curs_date >= '" + after + "' and c.curs_date  <= '" + before + "' \n" +
                "group by concat(c.convert_to,' (',cu.name,')'), concat(convert_from)", ((rs, row) -> map(rs)));
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
