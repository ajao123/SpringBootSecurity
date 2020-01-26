package com.allissonjardel.projeto.controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.allissonjardel.projeto.model.Medico;
import com.allissonjardel.projeto.model.Usuario;
import com.allissonjardel.projeto.service.MedicoService;
import com.allissonjardel.projeto.service.impl.UsuarioServiceImpl;

@Controller
@RequestMapping("medicos")
public class MedicoController {
	
	@Autowired
	private MedicoService service;
	
	@Autowired
	private UsuarioServiceImpl usuarioService;
	
	// abrir pagina de dados pessoais de medicos pelo MEDICO
	@GetMapping({"/dados"})
	public String abrirPorMedico(Medico medico, ModelMap model, @AuthenticationPrincipal User user) {
		
		Usuario usuario = usuarioService.findByEmail(user.getUsername());
		Optional<Medico> optMedico = service.findByUsuarioId(usuario.getId());
		if(optMedico.isPresent()) {
			medico = optMedico.get();
			model.addAttribute("medico", medico);
		}
		
		return "medico/cadastro";
	}
	
	// salvar medico
	@PostMapping("/salvar")
	public String salvar(Medico entity, RedirectAttributes attr, @AuthenticationPrincipal User user) {
		if( entity.hasNotId() && entity.getUsuario().hasNotId()) {
			Usuario usuario = usuarioService.findByEmail(user.getUsername());
			entity.setUsuario(usuario);
		}
		service.save(entity);
		attr.addFlashAttribute("sucesso","Operação realizada com sucesso.");
		attr.addFlashAttribute("medico", entity);
		return "redirect:/medicos/dados";
	}
	
	// salvar medico
	@PostMapping("/editar")
	public String editar(Medico entity, RedirectAttributes attr) {
		service.update(entity);
		attr.addFlashAttribute("sucesso","Operação realizada com sucesso.");
		attr.addFlashAttribute("medico", entity);
		return "redirect:/medicos/dados";
	}

	@GetMapping("/id/{idMedico}/excluir/especializacao/{idEspecialidade}")
	public String deletarEspecializacao(@PathVariable("idMedico") Long idMedico, 
									    @PathVariable("idEspecialidade") Long idEspecialidade,
									    RedirectAttributes attr){
		if(service.existeEspecialidadeAgendada(idMedico, idEspecialidade)) {
			attr.addFlashAttribute("falha", "Tem consultas agendadas, exclusão negada.");
		}else {
			service.removeSpecialization(idMedico, idEspecialidade);
			attr.addFlashAttribute("sucesso", "Especialidade removida com sucesso.");
		}
		
		return "redirect:/medicos/dados";
	}
	
	//Buscar Medicos por especialidade via ajax
	@GetMapping("/especialidade/titulo/{titulo}")
	public ResponseEntity<?> getMedicosPorEspecialidade(@PathVariable("titulo") String titulo) {
		return ResponseEntity.ok(service.findMedicosByEspeciality(titulo));
	}
	
//	@GetMapping("/id/{idMedico}/excluir/especializacao/{idEspecialidade}")
//	public ResponseEntity<?> deletarEspecializacao(@PathVariable("idMedico") Long idMedico, 
//									    @PathVariable("idEspecialidade") Long idEspecialidade,
//									    RedirectAttributes attr){
//		service.removeSpecialization(idMedico, idEspecialidade);
//		return ResponseEntity.ok().build();
//	}

}











