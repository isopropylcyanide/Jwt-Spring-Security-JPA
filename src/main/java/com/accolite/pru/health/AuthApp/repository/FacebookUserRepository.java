package com.accolite.pru.health.AuthApp.repository;

import com.accolite.pru.health.AuthApp.model.social.FacebookUser;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FacebookUserRepository extends JpaRepository<FacebookUser, Long> {
}
