package com.accolite.pru.health.AuthApp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.accolite.pru.health.AuthApp.model.User;

@Transactional
@Repository
public interface ForgotPasswordRepository extends JpaRepository<User, Long> {

	@Modifying
	@Query(value = "update USER set PASSWORD = :password where EMAIL= :email", nativeQuery = true)
	public void resetPassword(@Param("email") String email, @Param("password") String password);

	@Query(value = "select username from USER where EMAIL= :email", nativeQuery = true)
	String findUsernameByEmail(@Param("email") String email);

	Boolean existsByEmail(String email);
}
