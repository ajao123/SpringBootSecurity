package com.allissonjardel.projeto.service;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

import com.allissonjardel.projeto.model.Paciente;

public interface PacienteService {
	
	void save(Paciente entity);
	void update(Paciente entity);
	void delete(Long id);
	List<Paciente> getAll();
	Map<String, Object> buscarPacientes(HttpServletRequest request);
	Paciente findById(Long id);
	Optional<Paciente> findByPacienteId(Long id);
	Long findIdbyUsername(String username);
	Optional<Paciente> findPacientebyUsername(String username);
	Integer existByName(String nome);
}
