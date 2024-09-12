package api.ourwallet.repositories;

import api.ourwallet.domains.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import api.ourwallet.domains.User;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


public interface UserRepository extends JpaRepository<User, String> {
    Optional<User> findByEmail(String email);
    Optional<List<User>> findUsersByCategories(List<Category> categories);
}
