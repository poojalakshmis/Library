package com.collabera.library.repository;

import com.collabera.library.entity.BorrowerDetails;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BorrowerDetailsRepository extends JpaRepository<BorrowerDetails, Long> {
    Optional<BorrowerDetails> findByEmail(String email);
}