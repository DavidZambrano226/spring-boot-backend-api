package com.backend.prueba.service;

import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.backend.prueba.entity.UsuarioEntity;
import com.backend.prueba.repository.UsuarioRepository;

@Service
public class UsuarioService implements UserDetailsService {
	
	private Logger logger = LoggerFactory.getLogger(UsuarioService.class);

	@Autowired
	private UsuarioRepository usuarioRepository;
	
	public UsuarioService() {
		// TODO Auto-generated constructor stub
	}

	@Override
	@Transactional(readOnly = true)
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		// TODO Auto-generated method stub
		UsuarioEntity usuario = (UsuarioEntity) usuarioRepository.findByUsuario(username);
		
		if (usuario == null) {
			logger.error("Error en el login: no existe el usuario "+ usuario.getUsuario() +" en la base de datos.");
			throw new UsernameNotFoundException("Error en el login: no existe el usuario "+ usuario.getUsuario() +" en la base de datos.");
		}
		
		List<GrantedAuthority> authorities = usuario.getRoles()
				.stream()
				.map(rol -> new SimpleGrantedAuthority(rol.getNombre()))
				.peek(authority -> logger.info("Rol : "+ authority.getAuthority()))
				.collect(Collectors.toList());
		
		return new User(usuario.getUsuario(), usuario.getContrasena(), usuario.getEnabled(), true, true, true, authorities);
	}

}
