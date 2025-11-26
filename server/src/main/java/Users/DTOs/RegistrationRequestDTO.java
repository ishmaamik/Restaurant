package Users.DTOs;

import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

//MUST NEVER BE ENTITY I.E. DTOs are NEVER PERSISTED
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RegistrationRequestDTO {

    String email;
    String username;
    String password;

}
