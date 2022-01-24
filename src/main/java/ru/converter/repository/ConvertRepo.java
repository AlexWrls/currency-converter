package ru.converter.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.converter.entity.Convert;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface ConvertRepo extends JpaRepository<Convert, Long> {
    List<Convert> findAllByCursDateBetween(LocalDate after,LocalDate before);
}
