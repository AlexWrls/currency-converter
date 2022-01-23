package ru.converter.entity;

import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalDate;

/**
 * Сущьность стоимости валют для текущей даты
 */
@Entity
@Data
@Table(name = "RATES")
public class Rate {
    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    @Column(name = "ID")
    private String id;

    @Column(name = "CHAR_CODE")
    private String charCode;

    @Column(name = "VALUE")
    private double value;

    @Column(name = "CURS_DATE")
    private LocalDate cursDate;
}
