package com.henriquelavor.sigpedidos.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

import com.henriquelavor.sigpedidos.domain.Estado;
import com.henriquelavor.sigpedidos.dto.EstadoDTO;
import com.henriquelavor.sigpedidos.repositories.EstadoRepository;
import com.henriquelavor.sigpedidos.services.exceptions.DataIntegrityException;
import com.henriquelavor.sigpedidos.services.exceptions.ObjectNotFoundException;

@Service
public class EstadoService {

	@Autowired
	private EstadoRepository repo;

	public Estado find(Integer id) {
		Estado obj = repo.findOne(id);

		if (obj == null) {
			throw new ObjectNotFoundException(
					"Objeto não encontrado! Id: " + id + ", Tipo: " + Estado.class.getName());
		}

		return obj;
	}

	public Estado insert(Estado obj) {
		obj.setId(null);
		return repo.save(obj);
	}

	public Estado update(Estado obj) {
		Estado newObj = find(obj.getId());
		updateData(newObj, obj);
		return repo.save(newObj);
	}
	
	
	public void delete(Integer id) {
		find(id);
		try {
			repo.delete(id);
		} catch (DataIntegrityViolationException e) {
			throw new DataIntegrityException("Não é possível excluir um Estado que possui Cidades!");
		}
	}

	public List<Estado> findAll() {
		return repo.findAllByOrderByNome();
	}
	
	public Page<Estado> findPage(Integer page, Integer linesPerPage, String orderBy, String direction){
		PageRequest pageRequest = new PageRequest(page, linesPerPage, Direction.valueOf(direction), orderBy);
		return repo.findAll(pageRequest);
	}
	
	public Estado fromDTO(EstadoDTO objDto) {
		return new Estado(objDto.getId(), objDto.getNome());
	}
	
	private void updateData(Estado newObj, Estado obj) {
		newObj.setNome(obj.getNome());
	}
	
}
