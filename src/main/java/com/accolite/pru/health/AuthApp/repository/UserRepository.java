package com.accolite.pru.health.AuthApp.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import com.accolite.pru.health.AuthApp.model.User;

@Transactional
public interface UserRepository extends JpaRepository<User, Long> {

	Optional<User> findByUsername(String username);

	Boolean existsByEmail(String email);

	Optional<User> findByEmail(String email);

	Boolean existsByUsername(String username);

	@Modifying
	@Query(value = "update USER set PASSWORD = :password where EMAIL= :email", nativeQuery = true)
	public void resetPassword(@Param("email") String email, @Param("password") String password);
}
