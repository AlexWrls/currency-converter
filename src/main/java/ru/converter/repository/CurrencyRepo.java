package ru.converter.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.converter.entity.Currency;

@Repository
public interface CurrencyRepo extends JpaRepository<Currency, String> {
    Currency findByCharCode(String charCode);
}
