package com.bancoBci.ejercicios.bancoBci;

import com.bancoBci.ejercicios.bancoBci.entities.Persona;
import com.bancoBci.ejercicios.bancoBci.repository.PersonaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.List;

@SpringBootApplication
public class BancoBciApplication implements CommandLineRunner {

	@Autowired
	private PersonaRepository personaRepository;

	public static void main(String[] args) {
		SpringApplication.run(BancoBciApplication.class, args);

		Persona persona = new Persona();
		persona.setId(1L);
			}

	@Override
	public void run(String... args) throws Exception {
		personaRepository.save(new Persona(1L,"Michi",05));
		personaRepository.save(new Persona(2L,"Michi2",03));
		personaRepository.save(new Persona(3L,"Michi3",07));

		//Mostramos el numero de personas
		System.out.println("Numero de personas:" + personaRepository.count());

		//Mostramos la lista de personas
		List<Persona> personas =personaRepository.findAll();
		personas.forEach(p-> System.out.println("Nombre de la persona:"+p.getNombre()));

	}
}
