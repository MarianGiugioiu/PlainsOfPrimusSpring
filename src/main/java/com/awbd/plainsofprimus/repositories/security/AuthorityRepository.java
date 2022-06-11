package com.awbd.plainsofprimus.repositories.security;

import com.awbd.plainsofprimus.domain.security.Authority;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuthorityRepository extends JpaRepository<Authority, Long> {
}
