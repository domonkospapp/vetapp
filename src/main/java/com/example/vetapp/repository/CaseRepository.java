package com.example.vetapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.vetapp.model.Case;

@Repository
public interface CaseRepository extends JpaRepository<Case, Long> {
}
