package ru.converter.entity;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Сущьность курса валют
 */
@Entity
@Data
@Table(name = "CURRENCYS")
public class Currency {
    @Id
    @Column(name = "ID")
    private String id;

    @Column(name = "NUM_CODE")
    private String numCode;

    @Column(name = "CHAR_CODE")
    private String charCode;

    @Column(name = "NOMINAL")
    private int nominal;

    @Column(name = "NAME")
    private String name;

    @Column(name = "VALUE")
    private double value;

}
