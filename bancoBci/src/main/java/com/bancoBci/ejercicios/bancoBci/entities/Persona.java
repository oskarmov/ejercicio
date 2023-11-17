package com.bancoBci.ejercicios.bancoBci.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor  //constructor sin ningun campo
@AllArgsConstructor //contructor que incluye todos los atributos (id,nombre, edad)
@Data   //getter y setter
@Entity
@Table(name="registro")
public class Persona {
    @Id
    private Long id;
    private String nombre;
    private int edad;
}
