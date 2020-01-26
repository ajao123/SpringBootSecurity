package com.allissonjardel.projeto.service.impl;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.allissonjardel.projeto.model.Paciente;
import com.allissonjardel.projeto.repository.PacienteRepository;
import com.allissonjardel.projeto.service.PacienteService;

@Service
public class PacienteServiceImpl implements PacienteService{

	@Autowired
	PacienteRepository repository;
	
	@Override
	public void save(Paciente entity) {
		repository.save(entity);
	}

	@Override
	public void update(Paciente entity) {
//		Medico entity2 = repository.findById(entity.getId()).get();
//		entity2.setCrm(entity.getCrm());
//		entity2.setDtInscricao(entity.getDtInscricao());
//		entity2.setNome(entity.getNome());
//		
//		if(!entity.getEspecialidades().isEmpty()) {
//			entity2.getEspecialidades().addAll(entity.getEspecialidades());
//		}
//		
	}

	@Override
	public void delete(Long id) {
		repository.deleteById(id);
		
	}

	@Override
	public List<Paciente> getAll() {
		return repository.findAll();
	}

	@Override
	public Map<String, Object> buscarPacientes(HttpServletRequest request) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Paciente findById(Long id) {
		return repository.findById(id).get();
	}

	@Override
	public Optional<Paciente> findByPacienteId(Long id) {
		return repository.findByPacienteId(id);
	}

	@Override
	public Long findIdbyUsername(String username) {
		return repository.findIdbyUsername(username);
	}

	@Override
	public Optional<Paciente> findPacientebyUsername(String username) {
		return repository.findPacientebyUsername(username);
	}

	@Override
	public Integer existByName(String nome) {
		return repository.existByName(nome);
	}

}
