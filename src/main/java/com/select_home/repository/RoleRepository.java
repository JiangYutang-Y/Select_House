package com.Select_Home.Repository;

import com.Select_Home.Entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RoleRepository extends JpaRepository<Role,Long> {
    List<Role> findAllByUserId(Long id);
}
