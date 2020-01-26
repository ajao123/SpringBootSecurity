package com.allissonjardel.projeto.controller;

import java.util.Arrays;
import java.util.List;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.allissonjardel.projeto.model.Medico;
import com.allissonjardel.projeto.model.Perfil;
import com.allissonjardel.projeto.model.PerfilTipo;
import com.allissonjardel.projeto.model.Usuario;
import com.allissonjardel.projeto.service.MedicoService;
import com.allissonjardel.projeto.service.impl.UsuarioServiceImpl;


@Controller
@RequestMapping("u")
public class UsuarioController {

	@Autowired 
	UsuarioServiceImpl service;
	
	@Autowired 
	MedicoService medicoService;
	
	
	// abrir cadastro de usuarios (medico/admin/paciente)
	@GetMapping("/novo/cadastro/usuario")
	public String abrirPorAdminParaAdminMedicoPaciente(Usuario usuario) {
		return "usuario/cadastro";
	}
	
	@GetMapping("/lista")
	public String listarUsuarios() {
		return "usuario/lista";
	}
	
	@GetMapping("/datatables/server/usuarios")
	public ResponseEntity<?> listUsuariosDataTables(HttpServletRequest request){
		return ResponseEntity.ok(service.buscarUsuarios(request));
	}
	
	@PostMapping("/cadastro/salvar")
	public String salvarUsuarios(Usuario entity, RedirectAttributes attr) {
		List<Perfil> perfis = entity.getPerfis();
		if(perfis.size() > 2 || 
				// Administrador e Paciente
			   perfis.containsAll(Arrays.asList(new Perfil(1L), new Perfil(3L))) ||
			    // Paciente e Médico
			   perfis.containsAll(Arrays.asList(new Perfil(2L), new Perfil(3L)))
		  ) {
			
			attr.addFlashAttribute("falha", "Paciente não pode ser Administrador e/ou Médico.");
			attr.addFlashAttribute("usuario", entity);
		}else {
			
			try {
				service.save(entity);
				attr.addFlashAttribute("sucesso", "Operação realizada com sucesso !!!");
			}catch(DataIntegrityViolationException ex) {
				attr.addFlashAttribute("falha","Cadastro não realizado, email já existente.");
			}	
		}
		return "redirect:/u/novo/cadastro/usuario";
	}
	
	@GetMapping("/editar/credenciais/usuario/{id}")
	public ModelAndView preEditar(@PathVariable("id") Long id) {
		ModelAndView model = new ModelAndView("usuario/cadastro");
		model.addObject("usuario", service.findById(id));
		return model;
	}
	
	// pre edicao de cadastro de usuarios
	@GetMapping("editar/dados/usuario/{id}/perfis/{perfis}")
	public ModelAndView preEditarCadastroDadosPessoais(@PathVariable("id") Long id,
													   @PathVariable("perfis") Long[] perfisId,
													   @AuthenticationPrincipal User user) {
	//	System.out.println("Username: " + user.getUsername());
	//	System.out.println("Id do usuario: " + medicoService.findIdbyUsername(user.getUsername()));
	// Atenção Atenção Atenção, Verificar se a url não foi alterada
		
		Usuario usuario = service.buscarPorIdEPerfis(id, perfisId);
		
		if(usuario.getPerfis().contains(new Perfil(PerfilTipo.ADMIN.getCod())) &&
		   !usuario.getPerfis().contains(new Perfil(PerfilTipo.MEDICO.getCod()))		) {
			
			return new ModelAndView("usuario/cadastro", "usuario", usuario);
		}else if(usuario.getPerfis().contains(new Perfil(PerfilTipo.MEDICO.getCod()))) {
			
			Medico medico = medicoService.buscarUsuarioPorId(id);
			System.out.println(medico);
			return medico.hasNotId() ? new ModelAndView("medico/cadastro", "medico", new Medico(new Usuario(id)))
									 : new ModelAndView("medico/cadastro", "medico", medico);
			
		}else if(usuario.getPerfis().contains(new Perfil(PerfilTipo.PACIENTE.getCod()))) {
			ModelAndView model = new ModelAndView("error");
			model.addObject("status", 403);
			model.addObject("error", "Área Restrita");
			model.addObject("message", "Os dados de pacientes são restritos a ele;");
			return model;
		}
		
		return new ModelAndView("usuario/lista");
	}
	
	@GetMapping("/editar/senha")
	public String abrirEditarSenha() {
		return "usuario/editar-senha";
	}
	
	@PostMapping("/confirmar/senha")
	public String editarSenha(@RequestParam("senha1") String s1, @RequestParam("senha2") String s2,
							  @RequestParam("senha3") String s3, @AuthenticationPrincipal User user,
							  RedirectAttributes attr) {
		if(!s1.equals(s2)) {
			attr.addFlashAttribute("falha", "Senhas não conferem, tente novamente.");
			return "redirect:/u/editar/senha";
		}
		
		Usuario u = service.findByEmail(user.getUsername());
		if(!UsuarioServiceImpl.isSenhaCorreta(s3, u.getSenha())) {
			attr.addFlashAttribute("falha", "Senhas atual não confere, tente novamente.");
			return "redirect:/u/editar/senha";
		}
		
		service.alterarSenha(u, s1);
		
		attr.addFlashAttribute("sucesso", "Senha alterada com sucesso.");
		return "redirect:/u/editar/senha";
	}
	
	// abrir página de novo cadastro do paciente
	@GetMapping("/novo/cadastro")
	public String novoCadastro(Usuario usuario) {
		return "cadastrar-se";
	}
	
	// pagina de resposta do cadastro de paciente
	@GetMapping("/cadastro/realizado")
	public String cadastroRealizado() {
		return "fragments/mensagem";
	}
	
	//recebe o form da página cadastrar-se
	@PostMapping("/cadastro/paciente/salvar")
	public String salvarCadastroPaciente(Usuario usuario, BindingResult result) throws MessagingException {
		try {
			service.salvarCadastroPaciente(usuario);
		}catch(DataIntegrityViolationException ex) {
			result.reject("email", "Ops ... Este e-mail já existe na base de dados.");
			return "cadastrar-se";
		}
		return "redirect:/u/cadastro/realizado";
		
	}
	
	@GetMapping("/confirmacao/cadastro")
	public String respostaConfirmacaoCadastroPaciente(@RequestParam("codigo") String codigo, 
			RedirectAttributes attr) {
		
		service.ativarCadastroPaciente(codigo);
		attr.addFlashAttribute("alerta", "sucesso");
		attr.addFlashAttribute("titulo","Cadastro Ativado !!!");
		attr.addFlashAttribute("texto","Parabéns, seu cadastro está ativo.");
		attr.addFlashAttribute("subtexto", "Siga com seu login/senha.");
		
		return "redirect:/login";
	}
	
	//abre a pagina de pedido de redefinicao de senha
	@GetMapping("/p/redefinir/senha")
	public String pedidoRedefinirSenha() {
		return "usuario/pedido-recuperar-senha";
	}
	
	//form de pedido de rucuperar senha
	@GetMapping("/p/recuperar/senha")
	public String redefinirSenha(String email, ModelMap model) throws MessagingException {
		service.pedidoRedefinicaoDeSenha(email);
		model.addAttribute("sucesso","Em instantes você receberá um e-mail para "
				+ "prosseguir com a redifinição de sua senha.");
		model.addAttribute("usuario", new Usuario(email));
		
		return "usuario/recuperar-senha";
	}
	//salvar a nova senha via recuperacao de senha
	
	@PostMapping("/p/nova/senha")
	public String confirmacaoDeRedefinicaoDeSenha(Usuario usuario, ModelMap model) {
		Usuario u = service.findByEmail(usuario.getEmail());
		if(!usuario.getCodigoVerificador().equals(u.getCodigoVerificador())) {
			model.addAttribute("falha", "Código verificador não confere;");
			return "usuario/recuperar-senha";
		}
		
		u.setCodigoVerificador(null);
		service.alterarSenha(u, usuario.getSenha());
		model.addAttribute("sucesso","Funcionou legal.");
		model.addAttribute("alerta", "sucesso");
		model.addAttribute("titulo", "Senha redefinida!!!");
		model.addAttribute("texto", "Você já pode logar no sistema.");
		return "login";
	}
	
}



















