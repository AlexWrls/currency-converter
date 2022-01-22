package ru.converter.entity;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalDate;

@Data
@Entity
@Table(name = "CONVERTS")
public class Convert {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;

    @Column(name = "CONVERT_FROM")
    private String convertFrom;

    @Column(name = "CONVERT_TO")
    private String convertTo;

    @Column(name = "VALUE_FROM")
    private double valueFrom;

    @Column(name = "VALUE_TO")
    private double valueTo;

    @Column(name = "CURS_DATE")
    private LocalDate cursDate;
}
