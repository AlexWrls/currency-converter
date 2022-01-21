package ru.converter.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.converter.entity.CurrencyItem;

@Repository
public interface CurrencyItemRepo extends JpaRepository<CurrencyItem,String> {
}
