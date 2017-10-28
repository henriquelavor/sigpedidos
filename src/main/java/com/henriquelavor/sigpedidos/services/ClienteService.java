package com.henriquelavor.sigpedidos.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.henriquelavor.sigpedidos.domain.Cliente;
import com.henriquelavor.sigpedidos.repositories.ClienteRepository;
import com.henriquelavor.sigpedidos.services.exceptions.ObjectNotFoundException;

@Service
public class ClienteService {
	
	@Autowired
	private ClienteRepository repo;
	
	public Cliente buscar(Integer id) {
		Cliente obj = repo.findOne(id);
		
		if (obj == null) {
			throw new ObjectNotFoundException("Objeto não encontrado! Id: " + id
					+", Tipo: " + Cliente.class.getName());
		}
		
		return obj;
	}
}
