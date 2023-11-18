package cl.bci.ejercicios.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1")
public class Controlador {

  @Autowired
  private final UserService userService;
  private final UserToEntityMapper userToEntityMapper;
  private final UserToDtoMapper userToDtoMapper;
  private final UserToResponseMapper userToResponseMapper;


  @PostMapping("/users")
  public ResponseEntity<Map<String, Object>> addUser(@Valid @RequestBody UserDto userDto) {
    Map<String, Object> response = new HashMap<>();
    Persona persona = userService.saveUser(userToEntityMapper.toMap(userDto));
    response.put("user", userToResponseMapper.toMap(user));
    return ResponseEntity.status(HttpStatus.CREATED).body(response);
  }

  @DeleteMapping("/users/{userId}")
  public ResponseEntity<Void> deleteUser(@PathVariable String userId) {
    userService.deleteUser(userId);
    return new ResponseEntity<>(HttpStatus.NO_CONTENT);
  }

  @GetMapping("/users/{userId}")
  public ResponseEntity<Map<String, Object>> getUserById(@PathVariable String userId) {
    Map<String, Object> response = new HashMap<>();
    User user = userService.getUserById(userId);
    UserDto userDto = userToDtoMapper.toMap(user);
    userDto.setPhones(userService.getAllPhonesForUser(userId).stream()
        .map(phoneResponseToDtoMapper::toMap).toList()
    );
    response.put("user", userDto);
    return ResponseEntity.status(HttpStatus.OK).body(response);
  }
}

