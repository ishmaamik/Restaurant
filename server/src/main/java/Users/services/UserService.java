package Users.services;

import Users.DTOs.RegistrationRequestDTO;
import Users.domain.User;
import Users.enums.UserRole;
import Users.repository.UserRepo;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class UserService {

    @Autowired
    private UserRepo userRepo;
    @Autowired
    private PasswordEncoder passwordEncoder;

    //USER RETRIEVAL METHODS
    public User getUserByUserId(UUID userId){
        return userRepo.findById(userId)
                .orElseThrow(()-> new EntityNotFoundException("User not found"));
    }

    public User getUserByEmail(String email){
        return userRepo.findByEmail(email)
                .orElseThrow(()-> new EntityNotFoundException("User not found by this email"));
    }

    public User getUserByUsername(String username){
        return userRepo.findByUsername(username)
                .orElseThrow(()-> new EntityNotFoundException("User not found by this username"));
    }

    public List<User> getUsersByRole(UserRole role){
        return userRepo.findAllUsersByRole(role);
    }

    //GET ROLES OF A USER
    public Set<UserRole> getRoles(UUID userId){
        User user= getUserByUserId(userId);
        return user.getRoles();
    }

    public boolean isAdmin(UUID userId){
        return getRoles(userId).contains(UserRole.ADMIN);
    }

    public boolean HasRole(UserRole role, UUID userId){
        return getRoles(userId).contains(role);
    }

    //REGISTER AND USER MANAGEMENT

    @Transactional
    public User register(RegistrationRequestDTO request){

        if(userRepo.findByUsername(request.getUsername()).isPresent()){
            throw new EntityExistsException("User with username already exists");
        }

        if(userRepo.findByEmail(request.getEmail()).isPresent()){
            throw new EntityExistsException("User with email already exists");
        }

        User user= User.builder()
                .email(request.getEmail())
                .username(request.getUsername())
                .passwordHash(passwordEncoder.encode(request.getPassword()))
                .active(true)
                .roles(Set.of(UserRole.CUSTOMER))
                .build();
        return userRepo.save(user);
    }

    public void activate(UUID userId){
        User user= getUserByUserId(userId);
        user.setActive(true);
    }

    public void deactivate(UUID userId){
        User user= getUserByUserId(userId);
        user.setActive(false);
    }

    public boolean IsActive(UUID userId){
        User user= getUserByUserId(userId);
        return user.isActive();
    }


}
