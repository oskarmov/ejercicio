package com.bancoBci.ejercicios.bancoBci.service;

import com.bancoBci.ejercicios.bancoBci.entities.Persona;

import java.util.List;

public interface PersonaService {

    List<Persona> obtenerTodas();

    Persona obtenerPorId(Long id);

    Persona crearPersona(Persona persona);

}
