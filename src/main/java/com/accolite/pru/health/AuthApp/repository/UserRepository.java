package com.accolite.pru.health.AuthApp.repository;

import com.accolite.pru.health.AuthApp.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

	Optional<User> findByUsername(String username);

	Boolean existsByEmail(String email);

	@Query(value = "select U.* from user U join Facebook_User F on U.fb_id = F.fb_id "
			+ "	where U.email like :email or F.assoc_email like :email", nativeQuery = true)
	Optional<User> findByEmailOrAssociatedEmail(@Param("email") String email);

	Optional<User> findByEmail(String email);

	Boolean existsByUsername(String username);
}
