package com.allissonjardel.projeto.service;

import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import com.allissonjardel.projeto.model.Especialidade;

public interface EspecialidadeService {
	
	void save(Especialidade entity);
	void update(Especialidade entity, Long id);
	void delete(Long id);
	List<Especialidade> getAll();
	Map<String, Object> buscarEspecialidades(HttpServletRequest request);
	Especialidade findById(Long id);
	List<String> findEspecialidadeByTermo(String search);
	Especialidade findByTitulo(String search);
	Set<Especialidade> findByTitulos(String[] titulos);
	Map<String, Object> buscarEspecialidadesDoMedico(Long id, HttpServletRequest request);
	
}
