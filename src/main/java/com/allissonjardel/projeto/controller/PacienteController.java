package com.allissonjardel.projeto.controller;

import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.allissonjardel.projeto.model.Paciente;
import com.allissonjardel.projeto.model.Usuario;
import com.allissonjardel.projeto.service.PacienteService;
import com.allissonjardel.projeto.service.impl.UsuarioServiceImpl;
import com.allissonjardel.projeto.validator.PacienteValidator;

@Controller
@RequestMapping("pacientes")
public class PacienteController {

	@Autowired
	PacienteService service;
	
	@Autowired
	UsuarioServiceImpl usuarioService;
	
//	@InitBinder
//	public void initBinder(WebDataBinder binder) {
//		binder.addValidators(new PacienteValidator(service));
//	}
	
	// abrir paginas de dados pessoais do paciente
	@GetMapping("/dados")
	public String abrirPorPaciente(Paciente paciente, ModelMap model, @AuthenticationPrincipal User user) {
		
		Optional<Paciente> optPaciente = service.findPacientebyUsername(user.getUsername());
		
		if(optPaciente.isPresent()) {
			paciente = optPaciente.get();	
		}else {
			paciente.setUsuario(new Usuario(user.getUsername()));
		}
		model.addAttribute("paciente", paciente);
		return "paciente/cadastro";
	}	
	
	// salvar medico
	@PostMapping("/salvar")
	//public String salvar(@Valid Paciente entity, BindingResult result, RedirectAttributes attr) {
	public String salvar(@Valid Paciente entity, RedirectAttributes attr) {
		
		Usuario usuario = usuarioService.findByEmail(entity.getUsuario().getEmail());
		
//		if(result.hasErrors()) { 
//			return "paciente/cadastro";
//		}
		if(UsuarioServiceImpl.isSenhaCorreta(entity.getUsuario().getSenha(), usuario.getSenha() )) {
			entity.setUsuario(usuario);
			
			try {
				service.save(entity);
			}catch(DataIntegrityViolationException ex) {
				attr.addFlashAttribute("falha","Atributos inválidos, tente novamente.");
				return "redirect:/pacientes/dados";
			}
			
			//service.save(entity);
		}else {
			attr.addFlashAttribute("falha","Senhas digitada não confere, tente novamente..");
			return "redirect:/pacientes/dados";
		}
		
		attr.addFlashAttribute("sucesso","Operação realizada com sucesso.");
		attr.addFlashAttribute("paciente", entity);
		return "redirect:/pacientes/dados";
	}
	
	//editar medico
	@PostMapping("/editar")
	public String editar(Paciente entity, RedirectAttributes attr){
		
		Usuario usuario = usuarioService.findByEmail(entity.getUsuario().getEmail());
		
		if(UsuarioServiceImpl.isSenhaCorreta(entity.getUsuario().getSenha(), usuario.getSenha() )) {
			
			entity.setUsuario(usuario);
			service.save(entity);
			
		}else {
			attr.addFlashAttribute("falha","Senha digitada não confere, tente novamente.");
			return "redirect:/pacientes/dados";
		}
		
		attr.addFlashAttribute("sucesso","Operação realizada com sucesso.");
		attr.addFlashAttribute("medico", entity);
		return "redirect:/pacientes/dados";
		// return "paciente/cadastro";
	}
		
	
}
