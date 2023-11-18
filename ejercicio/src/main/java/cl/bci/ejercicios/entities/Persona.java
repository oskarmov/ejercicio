package cl.bci.ejercicios.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Data 
@Entity
@Table(name="registro")
public class Persona {

  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  private String id;
  private String name;
  @Column(unique = true)
  private String email;
  private String password;
  private LocalDateTime createdAt;
  private LocalDateTime modifiedAt;
  private LocalDateTime lastLoginAt;
  private Boolean isActive;
  private String token;
}
