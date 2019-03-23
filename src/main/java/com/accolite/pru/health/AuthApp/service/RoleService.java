package com.accolite.pru.health.AuthApp.service;

import com.accolite.pru.health.AuthApp.model.Role;
import com.accolite.pru.health.AuthApp.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
public class RoleService {

    private final RoleRepository roleRepository;

    @Autowired
    public RoleService(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    /**
     * Find all roles from the database
     */
    public Collection<Role> findAll() {
        return roleRepository.findAll();
    }

}
