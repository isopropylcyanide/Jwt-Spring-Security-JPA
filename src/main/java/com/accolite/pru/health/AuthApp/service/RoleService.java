package com.accolite.pru.health.AuthApp.service;

import com.accolite.pru.health.AuthApp.model.Role;
import com.accolite.pru.health.AuthApp.model.RoleName;
import com.accolite.pru.health.AuthApp.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class RoleService {

	@Autowired
	private RoleRepository roleRepository;


	public Optional<Role> findByName(RoleName roleName) {
		return roleRepository.findByRole(roleName);
	}
}
