package ru.converter.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.converter.entity.Convert;

@Repository
public interface ConvertRepo extends JpaRepository<Convert, Long> {
}
