package com.bancoBci.ejercicios.ejercicios.service;

import com.bancoBci.ejercicios.ejercicios.entities.Persona;

public interface PersonaService {

    Persona saveUser(Persona persona);
  
    Persona updateUser(String userId, Persona persona);

    Persona getUserById(String userId);

    Persona void deleteUser(String userId);

}
