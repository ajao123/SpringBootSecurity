package com.allissonjardel.projeto.controller;

import java.time.LocalDate;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.allissonjardel.projeto.model.Agendamento;
import com.allissonjardel.projeto.model.Especialidade;
import com.allissonjardel.projeto.model.Paciente;
import com.allissonjardel.projeto.model.PerfilTipo;
import com.allissonjardel.projeto.service.AgendamentoService;
import com.allissonjardel.projeto.service.EspecialidadeService;
import com.allissonjardel.projeto.service.PacienteService;


@Controller
@RequestMapping("agendamentos")
public class AgendamentoController {
	
	@Autowired
	private AgendamentoService service;
	
	@Autowired
	private EspecialidadeService especialidadeService;
	
	@Autowired
	private PacienteService pacienteService;
	
	@PreAuthorize("hasAnyAuthority('PACIENTE', 'MEDICO')")
	@GetMapping({"/agendar"})
	public String agenderConsulta(Agendamento agendamento){
		return "agendamento/cadastro";
	}

	@GetMapping("/horario/medico/{id}/data/{data}")
	@PreAuthorize("hasAnyAuthority('PACIENTE', 'MEDICO')")
	public ResponseEntity<?> getHorarios(@PathVariable("id") Long id,
										 @PathVariable("data") @DateTimeFormat(iso = ISO.DATE) LocalDate data){
		return ResponseEntity.ok(service.buscarHorariosNaoAgendadosPorMedicoIdEData(id, data));
	}
	
	@PostMapping("/salvar")
	@PreAuthorize("hasAuthority('PACIENTE')")
	public String salvar(Agendamento agendamento, @AuthenticationPrincipal User user, RedirectAttributes attr) {
		
		Especialidade especialidade = especialidadeService.findByTitulo(agendamento.getEspecialidade().getTitulo());
	//	Medico medico = medicoService.findById(agendamento.getMedico().getId()); Combo box ja envia o ID do medico
		Paciente paciente = pacienteService.findPacientebyUsername(user.getUsername()).get();
		
		agendamento.setEspecialidade(especialidade);
//		agendamento.setMedico(medico);
		agendamento.setPaciente(paciente);
		service.save(agendamento);
		attr.addFlashAttribute("sucesso", "Sua consulta foi agendada com sucesso.");
		return "redirect:/agendamentos/agendar";
	}
	
	@GetMapping({"/historico/paciente", "/historico/consultas"})
	@PreAuthorize("hasAnyAuthority('PACIENTE', 'MEDICO')")
	public String historico() {
		return "agendamento/historico-paciente";
	}
	
	@GetMapping("/datatables/server/historico")
	@PreAuthorize("hasAnyAuthority('PACIENTE', 'MEDICO')")
	public ResponseEntity<?> historicoAgendamentosPorPaciente(@AuthenticationPrincipal User user, HttpServletRequest request){
//		return ResponseEntity.ok(
//				service.buscarConsultasByIdPaciente(request, pacienteService.findIdbyUsername(user.getUsername()))
//		);
		
		if(user.getAuthorities().contains(new SimpleGrantedAuthority(PerfilTipo.PACIENTE.getDesc()))) {
			return ResponseEntity.ok(service.buscarConsultasPorPacienteEmail(user.getUsername(), request));
		}
		
		
		if(user.getAuthorities().contains(new SimpleGrantedAuthority(PerfilTipo.MEDICO.getDesc()))) {
			return ResponseEntity.ok(service.buscarConsultasPorMedicoEmail(user.getUsername(), request));	
		}
		
		return ResponseEntity.notFound().build();
	}

	//Formas de preEditar
	
//	@GetMapping({"/editar/consulta/{id}"})
//	public ModelAndView preEditarConsulta(@PathVariable("id") Long id) {
//		ModelAndView model =  new ModelAndView("agendamento/cadastro");
//		model.addObject("agendamento", service.findById(id));
//		return model;
//	}
	
	@GetMapping({"/editar/consulta/{id}"})
	@PreAuthorize("hasAnyAuthority('PACIENTE', 'MEDICO')")
	public String preEditarConsulta(@PathVariable("id") Long id, ModelMap model, @AuthenticationPrincipal User user) {
		
		Agendamento agendamento = service.findByIdAndPacienteOrMedicoEmail(id, user.getUsername());
		
		model.addAttribute("agendamento", agendamento);
		return "agendamento/cadastro";
	}
	
//	@GetMapping({"/editar/consulta/{id}"})
//	public String preEditarConsulta(@PathVariable("id") Long id, RedirectAttributes attr) {
//		attr.addFlashAttribute("agendamento", service.findById(id));
//		return "redirect:/agendamentos/agendar";
//	}
	
	@PostMapping("/editar")
	@PreAuthorize("hasAnyAuthority('PACIENTE', 'MEDICO')")
	public String editarConsulta(Agendamento agendamento, RedirectAttributes attr, @AuthenticationPrincipal User user) {	
		Especialidade especialidade = especialidadeService.findByTitulo(agendamento.getEspecialidade().getTitulo());
		agendamento.setEspecialidade(especialidade);
		service.update(agendamento, user.getUsername());
		attr.addFlashAttribute("sucesso", "Sua consulta foi alterada com sucesso.");
		return "redirect:/agendamentos/agendar";
	}
	
	@GetMapping({"/excluir/consulta/{id}"})
	@PreAuthorize("hasAnyAuthority('PACIENTE')")	// Dá autorização de paciente
	public String preEditarConsulta(@PathVariable("id") Long id, RedirectAttributes attr) {
		service.delete(id);
		attr.addFlashAttribute("sucesso", "Sua consulta foi excluida com sucesso.");
		return "redirect:/agendamentos/historico/paciente";
	}
	
}
