package com.henriquelavor.sigpedidos;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.henriquelavor.sigpedidos.domain.Categoria;
import com.henriquelavor.sigpedidos.domain.Cidade;
import com.henriquelavor.sigpedidos.domain.Cliente;
import com.henriquelavor.sigpedidos.domain.Endereco;
import com.henriquelavor.sigpedidos.domain.Estado;
import com.henriquelavor.sigpedidos.domain.Produto;
import com.henriquelavor.sigpedidos.domain.enums.TipoCliente;
import com.henriquelavor.sigpedidos.repositories.CategoriaRepository;
import com.henriquelavor.sigpedidos.repositories.CidadeRepository;
import com.henriquelavor.sigpedidos.repositories.ClienteRepository;
import com.henriquelavor.sigpedidos.repositories.EnderecoRepository;
import com.henriquelavor.sigpedidos.repositories.EstadoRepository;
import com.henriquelavor.sigpedidos.repositories.ProdutoRepository;


@SpringBootApplication
public class SigpedidosApplication implements CommandLineRunner{
	
	@Autowired
	private CategoriaRepository categoriaRepository;
	
	@Autowired
	private ProdutoRepository produtoRepository;
	
	@Autowired
	private EstadoRepository estadoRepository;
	
	@Autowired
	private CidadeRepository cidadeRepository;
	
	@Autowired
	private ClienteRepository clienteRepository;
	
	
	@Autowired
	private EnderecoRepository enderecoRepository;
	
	
	
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

		
		
		Estado est1 = new Estado(null, "Roraima");
		Estado est2 = new Estado(null, "São Paulo");

		Cidade c1 = new Cidade(null, "Boa Vista",est1);
		Cidade c2 = new Cidade(null, "São Paulo",est2);
		Cidade c3 = new Cidade(null, "Campinas",est2);
		
		est1.getCidades().addAll(Arrays.asList(c1));
		est2.getCidades().addAll(Arrays.asList(c2,c3));
		
		
		categoriaRepository.save(Arrays.asList(cat1,cat2));
		produtoRepository.save(Arrays.asList(p1, p2, p3));

		estadoRepository.save(Arrays.asList(est1,est2));
		cidadeRepository.save(Arrays.asList(c1,c2,c3));
		
		
		Cliente cli1 = new Cliente(null, "Maria Gabriela","maria@gmail.com","36378912377", TipoCliente.PESSOAFISICA);
		cli1.getTelefones().addAll(Arrays.asList("9536231178","95991611012"));
		
		Endereco e1 = new Endereco(null, "Rua Flores", "475", "Casa própria", "Centenário", "69305317", cli1, c1);
		Endereco e2 = new Endereco(null, "Avenida Matos", "105", "Sala 800", "Centro", "69305000", cli1, c2);
		
		cli1.getEnderecos().addAll(Arrays.asList(e1,e2));
		
		clienteRepository.save(Arrays.asList(cli1));
		enderecoRepository.save(Arrays.asList(e1,e2));
		
		
		
	}
	
	
	
	
}
