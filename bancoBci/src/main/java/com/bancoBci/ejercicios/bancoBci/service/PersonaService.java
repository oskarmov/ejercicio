package com.bancoBci.ejercicios.bancoBci.service;

import com.bancoBci.ejercicios.bancoBci.entities.Persona;

public interface PersonaService {

    Persona saveUser(Persona persona);
  
    Persona updateUser(String userId, Persona persona);

    Persona getUserById(String userId);

    Persona void deleteUser(String userId);

}
