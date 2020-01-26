package com.allissonjardel.projeto.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.allissonjardel.projeto.model.Agendamento;
import com.allissonjardel.projeto.model.Horario;

public interface AgendamentoService {
	
	void save(Agendamento entity);
	void update(Agendamento entity, String email);
	void delete(Long id);
	List<Agendamento> getAll();
	Agendamento findById(Long id);
	List<Horario> buscarHorariosNaoAgendadosPorMedicoIdEData(Long id, LocalDate data);
	List<Agendamento> findAgendamentosByIdPaciente(Long id);
	Map<String, Object> buscarConsultasPorPacienteEmail(String username, HttpServletRequest request);
	Map<String, Object> buscarConsultasPorMedicoEmail(String username, HttpServletRequest request);
	Agendamento findByIdAndPacienteOrMedicoEmail(Long id, String email);
}
