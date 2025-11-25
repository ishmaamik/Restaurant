package Users.services;

import Users.repository.UserRepo;
import Users.domain.User;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class UserService {
    private final UserRepo userRepo;

    public User getUserByUserId(UUID userId){
        return userRepo.findById(userId)
                .orElseThrow(()-> new EntityNotFoundException("User not found"));
    }
}
