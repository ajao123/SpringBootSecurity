package com.allissonjardel.projeto.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.allissonjardel.projeto.model.Especialidade;
import com.allissonjardel.projeto.service.EspecialidadeService;

@Controller
@RequestMapping("especialidades")
public class EspecialidadeController {
	
	@Autowired
	EspecialidadeService service;
	
	@GetMapping({"","/"})
	public String abrir(Especialidade especialidade) {
		return "especialidade/especialidade";
	}
	
	@PostMapping("/salvar")
	public String salvar(Especialidade entity, RedirectAttributes attr) {
		service.save(entity);
		attr.addFlashAttribute("sucesso", "Operação realizada com sucesso !!!");
		return "redirect:/especialidades";	
		
	}
	
	@GetMapping("/datatables/server")
	public ResponseEntity<?> getEspecialidades(HttpServletRequest request){
		return ResponseEntity.ok(service.buscarEspecialidades(request));
	}
	
	@GetMapping("/excluir/{id}")
	public String excluir(@PathVariable("id") Long id, RedirectAttributes attr) {
		service.delete(id);
		attr.addFlashAttribute("sucesso", "Operação realizada com sucesso !!!");
		return "redirect:/especialidades";	
		
	}
	
	@GetMapping("/editar/{id}")
	public ModelAndView preEditar(@PathVariable("id") Long id, RedirectAttributes attr) {
		
		ModelAndView model = new ModelAndView("especialidade/especialidade");
		attr.addFlashAttribute("especialidade", service.findById(id));
		return model;
	}
	
	
	
	@GetMapping("/titulo")
	public ResponseEntity<?> getEspecialidadesPorTermo(@RequestParam(name = "termo") String termo	) {
		return ResponseEntity.ok(service.findEspecialidadeByTermo(termo));
	}
	
	@GetMapping("/datatables/server/medico/{id}")
	public ResponseEntity<?> listUsuariosDataTables(@PathVariable("id") Long id, HttpServletRequest request){
		return ResponseEntity.ok(service.buscarEspecialidadesDoMedico(id, request));
	}
	
}














