package com.backend.prueba.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.backend.prueba.entity.ClienteEntity;

public interface ClienteRepository extends JpaRepository<ClienteEntity, Long> {
	
}
