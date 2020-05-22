package com.backend.prueba.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.backend.prueba.entity.ClienteEntity;
import com.backend.prueba.repository.ClienteRepository;

@Service
public class ClienteServiceImpl implements IClienteService{

	public ClienteServiceImpl() {
		// TODO Auto-generated constructor stub
	}

	@Autowired
	private ClienteRepository clienteDAO;
	
	@Override
	@Transactional(readOnly = true)
	public List<ClienteEntity> findAll() {
		// TODO Auto-generated method stub
		return (List<ClienteEntity>) clienteDAO.findAll();
	}
	
	@Override
	@Transactional(readOnly = true)
	public Page<ClienteEntity> findAll(Pageable pageable) {
		// TODO Auto-generated method stub
		return clienteDAO.findAll(pageable);
	}

	@Override
	@Transactional
	public ClienteEntity save(ClienteEntity cliente) {
		// TODO Auto-generated method stub
		return clienteDAO.save(cliente);
	}

	@Override
	@Transactional
	public void delete(Long id) {
		// TODO Auto-generated method stub
		clienteDAO.deleteById(id);
	}

	@Override
	@Transactional(readOnly = true)
	public ClienteEntity findById(Long id) {
		// TODO Auto-generated method stub
		return clienteDAO.findById(id).orElse(null);
	}

	

}
