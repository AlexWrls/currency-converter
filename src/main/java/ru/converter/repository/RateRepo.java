package ru.converter.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.converter.entity.Rate;

@Repository
public interface RateRepo extends JpaRepository<Rate, String> {
    Rate findTopByCharCodeOrderByIdDesc(String charCode);
}
