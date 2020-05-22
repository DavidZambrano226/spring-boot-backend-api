package com.backend.prueba.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.backend.prueba.entity.ClienteEntity;

public interface IClienteService {
	public List<ClienteEntity> findAll();
	
	public Page<ClienteEntity> findAll(Pageable pageable);
	
	public ClienteEntity save(ClienteEntity cliente);
	
	public void delete(Long id);
	
	public ClienteEntity findById(Long id);
}
