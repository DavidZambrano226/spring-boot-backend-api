package com.backend.prueba.repository;

import org.springframework.data.repository.CrudRepository;

public interface UsuarioRepository extends CrudRepository<UsuarioRepository, Long>{
	
	public UsuarioRepository findByUsuario(String usuario);
}
