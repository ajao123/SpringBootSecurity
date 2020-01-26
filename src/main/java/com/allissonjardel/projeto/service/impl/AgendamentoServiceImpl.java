package com.allissonjardel.projeto.service.impl;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import com.allissonjardel.projeto.datatables.Datatables;
import com.allissonjardel.projeto.datatables.DatatablesColunas;
import com.allissonjardel.projeto.exception.AcessoNegadoException;
import com.allissonjardel.projeto.model.Agendamento;
import com.allissonjardel.projeto.model.Horario;
import com.allissonjardel.projeto.repository.AgendamentoRepository;
import com.allissonjardel.projeto.repository.projection.HistoricoPaciente;
import com.allissonjardel.projeto.service.AgendamentoService;

@Service
public class AgendamentoServiceImpl implements AgendamentoService{

	@Autowired
	private AgendamentoRepository repository;
	
	@Autowired
	private Datatables datatables;
	
	@Override
	public void save(Agendamento entity) {
		repository.save(entity);
	}

	@Override
	public void update(Agendamento entity, String email) {
		
		
		Agendamento entity2 = findByIdAndPacienteOrMedicoEmail(entity.getId(), email);
		
		BeanUtils.copyProperties(entity, entity2, "id", "paciente");
		repository.save(entity2);
	}

	@Override
	public void delete(Long id) {
		repository.deleteById(id);
	}

	@Override
	public List<Agendamento> getAll() {
		return repository.findAll();	
	}

	@Override
	public Agendamento findById(Long id) {
		return repository.findById(id).get();
	}

	@Override
	public List<Horario> buscarHorariosNaoAgendadosPorMedicoIdEData(Long id, LocalDate data) {
		return repository.findByMedicoIdAndDataNotHorarioAgendado(id, data);
	}

	@Override
	public List<Agendamento> findAgendamentosByIdPaciente(Long id) {
		return repository.findAgendamentosByIdPaciente(id);
	}
	
	
	@Override
	public Map<String, Object> buscarConsultasPorPacienteEmail(String email, HttpServletRequest request) {
		datatables.setRequest(request);
		datatables.setColunas(DatatablesColunas.AGENDAMENTOS);
		Page<HistoricoPaciente> page = repository.findHistoricoByPacienteEmail(email, datatables.getPageable());
		return datatables.getResponse(page);
	}

	@Override
	public Map<String, Object> buscarConsultasPorMedicoEmail(String email, HttpServletRequest request) {
		datatables.setRequest(request);
		datatables.setColunas(DatatablesColunas.AGENDAMENTOS);
		Page<HistoricoPaciente> page = repository.findHistoricoByMedicoEmail(email, datatables.getPageable());
		return datatables.getResponse(page);
	}

	@Override
	public Agendamento findByIdAndPacienteOrMedicoEmail(Long id, String email) {
		return repository.findByIdAndPacienteOrMedicoEmail(id, email) 
				.orElseThrow(() -> new AcessoNegadoException("Acesso negado ao usuário: " + email));
	}
	
//	public Map<String, Object> buscarConsultasByIdPaciente(HttpServletRequest request, Long id) {
//		datatables.setRequest(request);
//		datatables.setColunas(DatatablesColunas.AGENDAMENTOS);
//		
//		Page<Agendamento> page = repository.findAgendamentosByIdPaciente(id, datatables.getPageable());
//		
//		return datatables.getResponse(page);
//	}

}
