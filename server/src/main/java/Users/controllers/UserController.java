package Users.controllers;

import Users.DTOs.RegistrationRequestDTO;
import Users.DTOs.UserDTO;
import Users.domain.User;
import Users.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {

    @Autowired
    private final UserService userService;

    public ResponseEntity<UserDTO> register(RegistrationRequestDTO registrationRequestDTO){
         User user= userService.register(registrationRequestDTO);
    }
}
