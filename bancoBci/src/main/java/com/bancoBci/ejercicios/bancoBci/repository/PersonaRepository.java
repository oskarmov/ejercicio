package com.bancoBci.ejercicios.bancoBci.repository;

import com.bancoBci.ejercicios.bancoBci.entities.Persona;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository //estereotipos
public interface PersonaRepository extends JpaRepository<Persona,Long> {

}
