package com.allissonjardel.projeto.service.impl;

import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.allissonjardel.projeto.datatables.Datatables;
import com.allissonjardel.projeto.datatables.DatatablesColunas;
import com.allissonjardel.projeto.model.Especialidade;
import com.allissonjardel.projeto.repository.EspecialidadeRepository;
import com.allissonjardel.projeto.service.EspecialidadeService;

@Service
public class EspecialidadeServiceImpl implements EspecialidadeService{

	@Autowired
	private EspecialidadeRepository repository;	
	
	@Autowired
	private Datatables datatables;
	
	@Override
	@Transactional(readOnly=false)
	public void save(Especialidade entity) {
		repository.save(entity);
	}

	@Override
	public void update(Especialidade entity, Long id) {
		repository.save(entity);
	}

	@Override
	@Transactional(readOnly=false)
	public void delete(Long id) {
		repository.deleteById(id);
	}

	@Override
	public List<Especialidade> getAll() {
		return repository.findAll();
	}
	
	@Override
	@Transactional(readOnly=true)
	public Map<String, Object> buscarEspecialidades(HttpServletRequest request) {
		datatables.setRequest(request);
		datatables.setColunas(DatatablesColunas.ESPECIALIDADES);
		
		Page<?> page = datatables.getSearch().isEmpty()
		? repository.findAll(datatables.getPageable()) 
		: repository.findAllByTitulo(datatables.getSearch(), datatables.getPageable());
		
		return datatables.getResponse(page);
	}

	@Override
	@Transactional(readOnly=true)
	public Especialidade findById(Long id) {
		return repository.findById(id).get();
	}

	@Override
	@Transactional(readOnly=true)
	public List<String> findEspecialidadeByTermo(String search) {
		return repository.findEspecialidadeByTermo(search);
	}

	@Override
	public Especialidade findByTitulo(String search) {
		return repository.findByTitulo(search);
	}

	@Override
	public Set<Especialidade> findByTitulos(String[] titulos) {
		return repository.findByTitulos(titulos);
	}

	@Transactional(readOnly=true)
	public Map<String, Object> buscarEspecialidadesDoMedico(Long id, HttpServletRequest request) {
		datatables.setRequest(request);
		datatables.setColunas(DatatablesColunas.ESPECIALIDADES);
		
		Page<Especialidade> page = repository.getEspecialidadesBy(id, datatables.getPageable());
		
		return datatables.getResponse(page);
	}
	
}
