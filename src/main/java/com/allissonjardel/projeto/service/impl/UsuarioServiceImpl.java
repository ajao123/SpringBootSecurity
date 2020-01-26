package com.allissonjardel.projeto.service.impl;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Base64Utils;

import com.allissonjardel.projeto.datatables.Datatables;
import com.allissonjardel.projeto.datatables.DatatablesColunas;
import com.allissonjardel.projeto.exception.AcessoNegadoException;
import com.allissonjardel.projeto.model.Perfil;
import com.allissonjardel.projeto.model.PerfilTipo;
import com.allissonjardel.projeto.model.Usuario;
import com.allissonjardel.projeto.repository.UsuarioRepository;

@Service
public class UsuarioServiceImpl implements UserDetailsService{
	
	@Autowired
	UsuarioRepository repository;
	
	@Autowired
	private Datatables datatables;
	
	@Autowired
	private EmailServiceImpl emailService;
	
	public Usuario findByEmail(String email) {
		return repository.findByEmail(email);
	}

	@Override
	@Transactional(readOnly=true)
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		Usuario usuario =  findByEmailAndAtivo(username)
				.orElseThrow(() -> new UsernameNotFoundException("Usuario " + username + " não encontrado."));
		return new User(
			usuario.getEmail(),
			usuario.getSenha(), 
			AuthorityUtils.createAuthorityList(getAuthorities(usuario.getPerfis()))	
		);
	}
	
	private String[] getAuthorities(List<Perfil> perfis) {
		String[] authorities = new String[perfis.size()];
		for(int i = 0; i < perfis.size(); i++) {
			authorities[i] = perfis.get(i).getDesc();
		}
		return authorities;
	}
	
	@SuppressWarnings("unused")
	private List<Usuario> getAll(){
		return repository.findAll();
	}
	
	@Transactional(readOnly=true)
	public Map<String, Object> buscarUsuarios(HttpServletRequest request) {
		datatables.setRequest(request);
		datatables.setColunas(DatatablesColunas.USUARIOS);
		
		Page<Usuario> page = datatables.getSearch().isEmpty() 
				?	repository.findAll(datatables.getPageable()) 
				:	repository.findByEmailOrPerfil(datatables.getSearch(), datatables.getPageable());
		
		return datatables.getResponse(page);
	}
	
	@Transactional(readOnly=true)
	public Usuario findById(Long id){
		return repository.findById(id).get();
	}
	
	@Transactional(readOnly=false)
	public void save(Usuario entity){
		String crypt = new BCryptPasswordEncoder().encode(entity.getSenha());
		entity.setSenha(crypt);
		repository.save(entity);
	}

	public Usuario buscarPorIdEPerfis(Long id, Long[] perfisId) {
		// TODO Auto-generated method stub
		return repository.findByIdAndPerfis(id, perfisId)
				.orElseThrow(() -> new UsernameNotFoundException("Usuário inexistente !"));
			
	}

	public static boolean isSenhaCorreta(String senhaDigitada, String senhaArmazenada) {
		
		return new BCryptPasswordEncoder().matches(senhaDigitada, senhaArmazenada);
	}

	public void alterarSenha(Usuario usuario, String senha) {
		usuario.setSenha(new BCryptPasswordEncoder().encode(senha));
		repository.save(usuario);
	}

	public void salvarCadastroPaciente(Usuario usuario) throws MessagingException {
		String crypt = new BCryptPasswordEncoder().encode(usuario.getSenha());
		usuario.setSenha(crypt);
		usuario.addPerfil(PerfilTipo.PACIENTE);
		repository.save(usuario);
		
		emailDeConfirmacaoDeCadastro(usuario.getEmail());
		
	}
	
	public Optional<Usuario> findByEmailAndAtivo(String email){
		return repository.findByEmailAndAtivo(email);
	}
	
	public void emailDeConfirmacaoDeCadastro(String email) throws MessagingException {
		String codigo = Base64Utils.encodeToString(email.getBytes());
		emailService.enviarPedidoDeConfirmacaoDeCadastro(email, codigo);
	}
	
	public void ativarCadastroPaciente(String codigo) {
		String email = new String(Base64Utils.decodeFromString(codigo));
		Usuario usuario = repository.findByEmail(email);
		
		if(usuario.hasNotId()) {
			throw new AcessoNegadoException("Não foi possível ativar seu cadastro. Entre em "
					+"contato com o suporte.");
		}
		usuario.setAtivo(true);
		repository.save(usuario);
	}
	
	@Transactional(readOnly=false)
	public void pedidoRedefinicaoDeSenha(String email) throws MessagingException {
		Usuario usuario = findByEmailAndAtivo(email).orElseThrow(() -> new UsernameNotFoundException("Usuario "
				+ email + "não encontrado."));
		String verificador = RandomStringUtils.randomAlphanumeric(6);
		usuario.setCodigoVerificador(verificador);
	//	repository.save(usuario);
		emailService.enviarPedidoRedefinicaoSenha(email, verificador);
	}
	
}
