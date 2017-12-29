package com.henriquelavor.sigpedidos.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.henriquelavor.sigpedidos.domain.Cliente;

@Repository
public interface ClienteRepository extends JpaRepository<Cliente, Integer>{
	//ele nao necessita que seja envolvida em uma transacao de banco de dados
	//diminui o locking nas transacoes de banco de dados
	@Transactional(readOnly=true) 
	Cliente findByEmail(String email);

}
