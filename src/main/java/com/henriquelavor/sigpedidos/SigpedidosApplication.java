package com.henriquelavor.sigpedidos;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.henriquelavor.sigpedidos.domain.Categoria;
import com.henriquelavor.sigpedidos.domain.Produto;
import com.henriquelavor.sigpedidos.repositories.CategoriaRepository;
import com.henriquelavor.sigpedidos.repositories.ProdutoRepository;


@SpringBootApplication
public class SigpedidosApplication implements CommandLineRunner{
	
	@Autowired
	private CategoriaRepository categoriaRepository;
	
	@Autowired
	private ProdutoRepository produtoRepository;

	public static void main(String[] args) {
		SpringApplication.run(SigpedidosApplication.class, args);
	}
	
	@Override
	public void run(String... arg0) throws Exception {
		
		Categoria cat1 =  new Categoria(null,"Informática");
		Categoria cat2 =  new Categoria(null,"Escritório");
		
		Produto p1 = new Produto(null, "Computador", 2000.00);
		Produto p2 = new Produto(null, "Impressora", 800.00);
		Produto p3 = new Produto(null, "Mouse", 80.00);
		
		cat1.getProdutos().addAll(Arrays.asList(p1,p2,p3));
		cat1.getProdutos().addAll(Arrays.asList(p2));
		
		p1.getCategorias().addAll(Arrays.asList(cat1));
		p2.getCategorias().addAll(Arrays.asList(cat1,cat2));
		p3.getCategorias().addAll(Arrays.asList(cat1));

		
		
		categoriaRepository.save(Arrays.asList(cat1,cat2));
		produtoRepository.save(Arrays.asList(p1, p2, p3));
		
	}
	
	
	
	
}
