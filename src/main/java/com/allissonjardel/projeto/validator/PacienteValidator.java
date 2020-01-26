package com.allissonjardel.projeto.validator;

import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.allissonjardel.projeto.model.Paciente;
import com.allissonjardel.projeto.service.PacienteService;

public class PacienteValidator implements Validator{

	PacienteService service;
	
	public PacienteValidator(PacienteService service) {
		this.service = service;	
	}
	
	@Override
	public boolean supports(Class<?> clazz) {
		return Paciente.class.equals(clazz);
	}

	@Override
	public void validate(Object object, Errors errors) {
		Paciente entity = (Paciente) object;
		
		if(entity.getId() == null) {
			if(service.existByName(entity.getNome()) > 0) {     
				System.out.println(service.existByName(entity.getNome()));
				errors.rejectValue("nome", "Existe.nome");
			}	
		}
	
	}

}
