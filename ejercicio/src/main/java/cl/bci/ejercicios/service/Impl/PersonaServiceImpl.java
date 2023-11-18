package com.bancoBci.ejercicios.ejercicios.service.Impl;

import com.bancoBci.ejercicios.ejercicios.entities.Persona;
import com.bancoBci.ejercicios.ejercicios.service.PersonaService;

import java.time.LocalDateTime;
import java.util.Optional;


public class PersonaServiceImpl implements PersonaService {

@Override
  public Persona saveUser(Persona persona) {
    LocalDateTime currentTime = LocalDateTime.now();
    Persona.setCreatedAt(currentTime);
    Persona.setLastLoginAt(currentTime);
    Persona.setIsActive(true);
    Persona.setPassword(passwordEncoder.encode(user.getPassword()));
    Persona.setToken(jwtService.generateToken(user));

    Optional<Persona> userOptional = userDao.saveUser(user);

    if (userOptional.isEmpty()){
      throw new UserException("Persona no agregada");
    }

    return userOptional.get();
  }

  @Override
  public Persona updateUser(String userId, Persona persona) {
    Optional<Persona> userOptional = userDao.findUserById(userId);
    if(userOptional.isEmpty()){
      throw new UserNotFoundException("No se pudo actualizar");
    }

    Persona userUpdate = userOptional.get();
    userUpdate.setName(user.getName());
    userUpdate.setModifiedAt(LocalDateTime.now());

    userOptional = userDao.saveUser(userUpdate);

    if (userOptional.isEmpty()){
      throw new UserException("No se pudo actualizar, volver a intentar");
    }

    return userOptional.get();
  }

  @Override
  public Persona getUserById(String userId) {
    Optional<Persona> userOptional = userDao.findUserById(userId);
    if (userOptional.isEmpty()){
      throw new UserNotFoundException("Usuario no encontrado");
    }
    return userOptional.get();
  }

  @Override
  public void deleteUser(String userId) {
    Optional<Persona> userOptional = userDao.findUserById(userId);
    if(userOptional.isEmpty()){
      throw new UserNotFoundException("Usuario no se encuentra en BD");
    }
    Persona userDelete = userOptional.get();
    userDelete.setModifiedAt(LocalDateTime.now());
    userDelete.setIsActive(false);
    userOptional = userDao.saveUser(userDelete);
    if(userOptional.isEmpty()){
      throw new UserException("No se pudo actualizar");
    }
  }
}

