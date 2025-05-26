package core.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import core.model.UserCredential;

@Repository
public interface UserCredentialRepository extends JpaRepository<UserCredential, String> {
	Optional<UserCredential> findByEmail(String email);
}
