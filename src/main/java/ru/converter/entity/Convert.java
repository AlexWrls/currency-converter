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
@Table(name = "convert")
public class Convert {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Long id;

    @Column(name = "convert_from")
    private String convertFrom;

    @Column(name = "convert_to")
    private String convertTo;

    @Column(name = "value_from")
    private double valueFrom;

    @Column(name = "value_to")
    private double valueTo;

    @Column(name = "curs_date")
    private LocalDate cursDate;
}
