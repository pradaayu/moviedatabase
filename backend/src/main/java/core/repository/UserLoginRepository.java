package core.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import core.model.UserLogin;

@Repository
public interface UserLoginRepository extends JpaRepository<UserLogin, String> {
	Optional<UserLogin> findByUserId(String userId);
}
