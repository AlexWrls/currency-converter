package ru.converter.entity;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalDate;


@Entity
@Data
@Table(name = "currency_item")
public class CurrencyItem {
    @Id
    @Column
    private String id;

    @Column(name = "num_code")
    private String numCode;

    @Column(name = "char_code")
    private String charCode;

    @Column(name = "nominal")
    private int nominal;

    @Column(name = "name")
    private String name;

    @Column(name = "value")
    private double value;

    @Column(name = "curs_date")
    private LocalDate cursDate;
}
