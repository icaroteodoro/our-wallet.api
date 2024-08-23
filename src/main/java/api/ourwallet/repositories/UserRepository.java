package api.ourwallet.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import api.ourwallet.domains.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, String> {
    Optional<User> findByEmail(String email);
}
