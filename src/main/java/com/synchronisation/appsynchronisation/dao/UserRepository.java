package com.synchronisation.appsynchronisation.dao;

import com.synchronisation.appsynchronisation.models.USR;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<USR, Long> {

    Optional<USR> findUSRByEmail(String email);
}
