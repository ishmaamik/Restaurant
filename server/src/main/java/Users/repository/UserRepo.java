package Users.repository;

import Users.enums.UserRole;
import Users.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepo extends JpaRepository<User, UUID> {
    Optional<User> findByEmail(String email);
    Optional<User> findByUsername(String username);
    List<User> findAllUsersByRole(UserRole userRole);
    //findAll() does FindAllUsers work
    //findAllUsersByEmail is unnecessary as findAll does the work
    //Optional is necessary otherwise throws error silently if not found
}
