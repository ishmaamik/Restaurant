package Users.controllers;

import Users.DTOs.RegistrationRequestDTO;
import Users.DTOs.UserDTO;
import Users.domain.User;
import Users.mapper.UserMapper;
import Users.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;
    @Autowired
    private UserMapper userMapper;

    @PostMapping("/register")
    public ResponseEntity<UserDTO> register(@RequestBody RegistrationRequestDTO registrationRequestDTO){
         User user= userService.register(registrationRequestDTO);
         return ResponseEntity.status(201).body(userMapper.MapUsertoUserDTO(user));
    }
}
