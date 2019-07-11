package com.example.vetapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.vetapp.model.Pet;

@Repository
public interface PetRepository extends JpaRepository<Pet, Long> {
}
