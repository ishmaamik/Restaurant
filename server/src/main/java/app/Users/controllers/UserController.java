package app.Users.controllers;

import app.Users.mapper.UserMapper;
import app.Users.DTOs.RegistrationRequestDTO;
import app.Users.DTOs.UserDTO;
import app.Users.domain.User;
import app.Users.services.UserService;
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
