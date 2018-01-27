package com.henriquelavor.sigpedidos.services;

import java.awt.image.BufferedImage;
import java.net.URI;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.henriquelavor.sigpedidos.domain.Cidade;
import com.henriquelavor.sigpedidos.domain.Cliente;
import com.henriquelavor.sigpedidos.domain.Endereco;
import com.henriquelavor.sigpedidos.domain.enums.Perfil;
import com.henriquelavor.sigpedidos.domain.enums.TipoCliente;
import com.henriquelavor.sigpedidos.dto.ClienteDTO;
import com.henriquelavor.sigpedidos.dto.ClienteNewDTO;
import com.henriquelavor.sigpedidos.repositories.CidadeRepository;
import com.henriquelavor.sigpedidos.repositories.ClienteRepository;
import com.henriquelavor.sigpedidos.repositories.EnderecoRepository;
import com.henriquelavor.sigpedidos.security.UserSS;
import com.henriquelavor.sigpedidos.services.exceptions.AuthorizationException;
import com.henriquelavor.sigpedidos.services.exceptions.DataIntegrityException;
import com.henriquelavor.sigpedidos.services.exceptions.ObjectNotFoundException;

@Service
public class ClienteService {
	
	@Autowired
	private BCryptPasswordEncoder pe;
	
	@Autowired
	private ClienteRepository repo;
	
	@Autowired
	private CidadeRepository cidadeRepository;
	
	@Autowired
	private EnderecoRepository enderecoRepository;
	
	@Autowired
	private S3Service s3Service;
	
	@Autowired
	private ImageService imageService;
	
	@Value("${img.prefix.client.profile}")
	private String prefix;
	
	@Value("${img.profile.size}")
	private Integer size;
	
	
	public Cliente find(Integer id) {
		
		UserSS user = UserService.authenticated();
		
		if (user==null || !user.hasRole(Perfil.ADMIN) && !id.equals(user.getId())) {
			 throw new AuthorizationException("Acesso negado");
		}
		
		Cliente obj = repo.findOne(id);
		
		if (obj == null) {
			throw new ObjectNotFoundException("Objeto não encontrado! Id: " + id
					+", Tipo: " + Cliente.class.getName());
		}
		
		return obj;
	}
	
	public Cliente insert(Cliente obj) {
		obj.setId(null);
		repo.save(obj);
		enderecoRepository.save(obj.getEnderecos());
		return obj;
	}

	
	public Cliente update(Cliente obj) {
		Cliente newObj = find(obj.getId());
		updateData(newObj, obj);
		return repo.save(newObj);
	}

	public void delete(Integer id) {
		find(id);
		try {
			repo.delete(id);
		} catch (DataIntegrityViolationException e) {
			throw new DataIntegrityException("Não é possível excluir o cliente, devido haver pedidos relacionados.");
		}
	}

	public List<Cliente> findAll() {
		return repo.findAll();
	}
	
	public Page<Cliente> findPage(Integer page, Integer linesPerPage, String orderBy, String direction){
		PageRequest pageRequest = new PageRequest(page, linesPerPage, Direction.valueOf(direction), orderBy);
		return repo.findAll(pageRequest);
	}
	
	public Cliente fromDTO(ClienteDTO objDto) {
		return new Cliente(objDto.getId(), objDto.getNome(), objDto.getEmail(),null, null, null);
	}
	
	public Cliente fromDTO(ClienteNewDTO objDto) {
		Cliente cli = new Cliente(null, objDto.getNome(), objDto.getEmail(), objDto.getCpfOuCnpj(),TipoCliente.toEnum(objDto.getTipo()), pe.encode(objDto.getSenha()));
		Cidade cid = cidadeRepository.findOne(objDto.getCidadeId());
		Endereco end = new Endereco(null, objDto.getLogradouro(), objDto.getNumero(), objDto.getComplemento(), objDto.getBairro(), objDto.getCep(), cli, cid);
		cli.getEnderecos().add(end); //para o cliente reconhecer os enderecos dele
		cli.getTelefones().add(objDto.getTelefone1());
		if (objDto.getTelefone2()!=null) {
			cli.getTelefones().add(objDto.getTelefone2());
		}
		if (objDto.getTelefone3()!=null) {
			cli.getTelefones().add(objDto.getTelefone3());
		}
		return cli;
	}
	
	private void updateData(Cliente newObj, Cliente obj) {
		newObj.setNome(obj.getNome());
		newObj.setEmail(obj.getEmail());
	}
	
	public URI uploadProfilePicture(MultipartFile multipartFile ) {
		
		UserSS user = UserService.authenticated(); //novo: salvando URL da imagem em cliente
		
		if (user == null) { //novo: salvando URL da imagem em cliente
			throw new AuthorizationException("Acesso negado"); //novo: salvando URL da imagem em cliente
		}
		
		BufferedImage jpgImage = imageService.getJpgImageFromFile(multipartFile);
		jpgImage = imageService.cropSquare(jpgImage);
		jpgImage = imageService.resize(jpgImage, size);
		
		String fileName = prefix + user.getId() + ".jpg";
		
		return s3Service.uploadFile(imageService.getInputStream(jpgImage, "jpg"), fileName, "image");
		//URI uri = s3Service.uploadFile(multipartFile); //novo: salvando URL da imagem em cliente
		//Cliente cli = repo.findOne(user.getId()); //novo: salvando URL da imagem em cliente
		//cli.setImageUrl(uri.toString()); //novo: salvando URL da imagem em cliente
		//repo.save(cli); //novo: salvando URL da imagem em cliente
		
		//return uri;  //novo: salvando URL da imagem em cliente
		//return s3Service.uploadFile(multipartFile);
	}

}
