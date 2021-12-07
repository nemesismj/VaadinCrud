package com.tutorial.vaadin.domain;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.UUID;

@Entity
@Data
public class Employee {
    @Id
    @GeneratedValue
    private UUID id;
    private String lastName;
    private String firstName;
    private String patronymic;
}
