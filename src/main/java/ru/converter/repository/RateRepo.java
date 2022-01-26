package ru.converter.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.converter.entity.Rate;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface RateRepo extends JpaRepository<Rate, String> {

    //    Rate findTopByCharCodeOrderByIdDesc(String charCode);
//
    List<Rate> findAllByCharCode(String charCode);
    Rate findByCharCode(String charCode);

    Rate findTopByOrderByIdDesc();

    Optional<Rate> findByCursDateAndCharCode(LocalDate curseDate, String charCode);
}
