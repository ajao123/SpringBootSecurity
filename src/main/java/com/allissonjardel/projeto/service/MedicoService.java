package com.allissonjardel.projeto.service;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

import com.allissonjardel.projeto.model.Medico;

public interface MedicoService {
	
	void save(Medico entity);
	void update(Medico entity);
	void delete(Long id);
	List<Medico> getAll();
	Map<String, Object> buscarMedicos(HttpServletRequest request);
	Medico findById(Long id);
	Medico buscarUsuarioPorId(Long id);
	Optional<Medico> findByUsuarioId(Long id);
	Long findIdbyUsername(String username);
	void removeSpecialization(Long idMedico, Long idEspecialidade);
	List<Medico> findMedicosByEspeciality(String titulo);
	boolean existeEspecialidadeAgendada(Long idMed, Long idEsp);
	
}
