package Users.repository;

import Users.domain.User;
import Users.enums.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface UserRepo extends JpaRepository<User, UUID> {
    List<User> findByEmail(String email);
    List<User> findByUsername(String username);
    List<User> findByRole(UserRole userRole);
}
