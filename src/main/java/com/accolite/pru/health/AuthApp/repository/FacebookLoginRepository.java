package com.accolite.pru.health.AuthApp.repository;

import com.accolite.pru.health.AuthApp.model.social.FacebookLogin;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FacebookLoginRepository extends JpaRepository<FacebookLogin, Long> {
}
