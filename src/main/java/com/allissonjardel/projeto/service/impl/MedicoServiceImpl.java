package com.allissonjardel.projeto.service.impl;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.allissonjardel.projeto.model.Medico;
import com.allissonjardel.projeto.repository.MedicoRepository;
import com.allissonjardel.projeto.service.MedicoService;

@Service
public class MedicoServiceImpl implements MedicoService{

	@Autowired
	MedicoRepository repository;
	
	@Override
	@Transactional(readOnly = false)
	public void save(Medico entity) {
		repository.save(entity);
	}

	@Override
	@Transactional(readOnly = false)
	public void update(Medico entity) {
		Medico entity2 = repository.findById(entity.getId()).get();
		entity2.setCrm(entity.getCrm());
		entity2.setDtInscricao(entity.getDtInscricao());
		entity2.setNome(entity.getNome());
		
		if(!entity.getEspecialidades().isEmpty()) {
			entity2.getEspecialidades().addAll(entity.getEspecialidades());
		}
		
	//	repository.save(entity2);
	}

	@Override
	public void delete(Long id) {
		repository.deleteById(id);
	}

	@Override
	public List<Medico> getAll() {
		return repository.findAll();
	}

	@Override
	public Map<String, Object> buscarMedicos(HttpServletRequest request) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Medico findById(Long id) {
		return repository.findById(id).get();
	}

	@Override
	public Medico buscarUsuarioPorId(Long id) {
		return repository.findByUsuarioId(id)
		.orElse(new Medico());
	}

	@Override
	public Optional<Medico> findByUsuarioId(Long id) {
		return repository.findByUsuarioId(id);
	}

	@Override
	public Long findIdbyUsername(String username) {
		return repository.findIdbyUsername(username);
	}

	@Override
	public void removeSpecialization(Long idMedico, Long idEspecialidade) {
		repository.removeSpecialization(idMedico, idEspecialidade);
	}

	@Override
	public List<Medico> findMedicosByEspeciality(String titulo) {
		return repository.findMedicosByEspeciality(titulo);
	}

	@Override
	public boolean existeEspecialidadeAgendada(Long idMed, Long idEsp) {
		
		return repository.hasEspecialidadeAgendada(idMed, idEsp).isPresent();
	}

}
