package ru.converter.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.converter.dto.Statistic;
import ru.converter.entity.Convert;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface ConvertRepo extends JpaRepository<Convert, Long> {

    @Query(value="select concat(c.convert_to,' (',currTo.name,')')as convertTo , concat(c.convert_from,' (',currFrom.name,')') as convertFrom , count(*) as count from converts as c " +
            "join currencys as currTo on  currTo.char_code like c.convert_to " +
            "join currencys as currFrom on  currFrom.char_code like c.convert_from " +
            "where c.convert_to like currTo.char_code and c.curs_date >= :after and c.curs_date <= :before " +
            "group by concat(c.convert_to,' (',currTo.name,')'), concat(c.convert_from,' (',currFrom.name,')')", nativeQuery=true)
    List<Statistic> getStatistic(@Param("after") LocalDate after,@Param("before") LocalDate before);
}
