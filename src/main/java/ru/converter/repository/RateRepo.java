package ru.converter.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.converter.entity.Rate;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface RateRepo extends JpaRepository<Rate, String> {

    Rate findByCharCode(String charCode);

    Rate findTopByOrderByCursDateDesc();

    Optional<Rate> findByCursDateAndCharCode(LocalDate curseDate, String charCode);

    List<Rate> findAllByCharCodeOrderByCursDate(String charCode);
}
