package cl.bci.ejercicios.repository;


import cl.bci.ejercicios.entities.Persona;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository 
public interface PersonaRepository extends JpaRepository<Persona,Long> {

}
