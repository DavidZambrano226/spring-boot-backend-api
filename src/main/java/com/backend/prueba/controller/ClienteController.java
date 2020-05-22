package com.backend.prueba.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.backend.prueba.entity.ClienteEntity;
import com.backend.prueba.service.IClienteService;

@RestController
@RequestMapping("/api")
public class ClienteController {
	
	@Autowired
	private IClienteService clienteService;

	public ClienteController() {
		// TODO Auto-generated constructor stub
	}
	
	@GetMapping("/clientes")
	public List<ClienteEntity> index(){
		return clienteService.findAll();
	}
	
	@GetMapping("/clientes/page/{page}")
	public Page<ClienteEntity> index(@PathVariable Integer page){
		Pageable pageable = PageRequest.of(page, 2);
		return clienteService.findAll(pageable);
	}
	
	@GetMapping("/clientes/{id}")
	public ResponseEntity<?> show(@PathVariable Long id) {
		
		ClienteEntity cliente = null;
		Map<String, Object> response = new HashMap<>();
		
		try {
			
			cliente = clienteService.findById(id);
			
		} catch (DataAccessException e) {
			// TODO: handle exception
			response.put("mensaje", "Error al realizar la consulta en la base de datos");
			response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}		
	
		if (cliente == null) {
			response.put("mensaje", "El cliente con el ID: ".concat(id.toString().concat(" No existe en la base de datos")));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
		}
		
		return new ResponseEntity<ClienteEntity>(cliente, HttpStatus.OK);
	}
	
	@PostMapping("/clientes")
	public ResponseEntity<?> create(@Valid @RequestBody ClienteEntity cliente, BindingResult result) {
		
		ClienteEntity clienteNuevo = null;
		Map<String, Object> response = new HashMap<>();
		
		if (result.hasErrors()) {

			//versiones anteriores a java 8
			/*List<String> errors = new ArrayList<>();
			for(FieldError err: result.getFieldErrors()) {
				errors.add("El campo '"+err.getField()+"' "+err.getDefaultMessage());
			}*/
			
			//nueva forma a partir de java 8 de iterar sin el for
			List<String> errors = result.getFieldErrors()
					.stream()
					.map(err -> {
							return "El campo '"+err.getField()+"' "+err.getDefaultMessage();
						})
					.collect(Collectors.toList());
			
			
			response.put("errors", errors);
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.BAD_REQUEST);
		}
		
		try {
			clienteNuevo = clienteService.save(cliente);
		} catch (DataAccessException e) {
			// TODO: handle exception
			response.put("mensaje", "Error al insertar el cliente en la base de datos");
			response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		response.put("mensaje", "El cliente ha sido creado con exito");
		response.put("data", clienteNuevo);
		
		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.CREATED);
	}
	
	@PutMapping("/clientes/{id}")
	public ResponseEntity<?> update(@Valid @RequestBody ClienteEntity cliente, BindingResult result, @PathVariable Long id) {
		ClienteEntity clienteActual = null;
		ClienteEntity clienteActualizado = null;
		
		Map<String, Object> response = new HashMap<>();
		
		if (result.hasErrors()) {

			//versiones anteriores a java 8
			/*List<String> errors = new ArrayList<>();
			for(FieldError err: result.getFieldErrors()) {
				errors.add("El campo '"+err.getField()+"' "+err.getDefaultMessage());
			}*/
			
			//nueva forma a partir de java 8 de iterar sin el for
			List<String> errors = result.getFieldErrors()
					.stream()
					.map(err -> {
							return "El campo '"+err.getField()+"' "+err.getDefaultMessage();
						})
					.collect(Collectors.toList());
			
			
			response.put("errors", errors);
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.BAD_REQUEST);
		}
		
		clienteActual = clienteService.findById(id);
		
		if (clienteActual == null) {
			response.put("mensaje", "Error: nose puede editar, el cliente con el ID: ".concat(id.toString().concat(" No existe en la base de datos")));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
		}
		
		try {
			
			clienteActual.setNombre(cliente.getNombre());
			clienteActual.setApellido(cliente.getApellido());
			clienteActual.setCorreo(cliente.getCorreo());
			
			clienteActualizado = clienteService.save(clienteActual);
		} catch (DataAccessException e) {
			// TODO: handle exception
			response.put("mensaje", "Error al actualizar el cliente en la base de datos");
			response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		response.put("mensaje", "El cliente ha sido actualizado con exito");
		response.put("data", clienteActualizado);
		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.CREATED);
	}
	
	@DeleteMapping("/clientes/{id}")

	public ResponseEntity<?> delete(@PathVariable Long id) {
		Map<String, Object> response = new HashMap<>();
		
		try {
			clienteService.delete(id);
			
		} catch (DataAccessException e) {
			// TODO: handle exception
			response.put("mensaje", "Error al eliminar el cliente en la base de datos");
			response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		response.put("mensaje", "El cliente ha sido eliminado con éxito!");
		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.OK);
	}
	
	

}
