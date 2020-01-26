package com.allissonjardel.projeto.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.allissonjardel.projeto.model.Agendamento;
import com.allissonjardel.projeto.model.Horario;
import com.allissonjardel.projeto.repository.projection.HistoricoPaciente;

public interface AgendamentoRepository extends JpaRepository<Agendamento, Long>{

	
	/*
	 *  Retorna todos os horarios que não estão presentes nas consultas do Médico X na Data Y
	 *  
	 *  Primeiro ele puxa todos os horários, e testará 1 por 1 todos os horários.
	 *  Ele buscará se existe alguma consulta que possua um medico com id A, data B e o horario passado.
	 *  Se não for encontrado nenhuma consulta com esses paramêtros, então o horário será adicionado na  
	 *  lista de horários disponíveis para a consulta. Abstrar a segunda parte do código.
	 */
	
	@Query("select h from Horario h where not exists(select a from Agendamento a where a.medico.id = :id and a.dataConsulta = :data and a.horario.id = h.id) order by h.horaMinuto asc")
	List<Horario> findByMedicoIdAndDataNotHorarioAgendado(Long id, LocalDate data);	

	@Query(value = "SELECT * FROM agendamentos where id_paciente = ?1", nativeQuery = true)
	List<Agendamento> findAgendamentosByIdPaciente(Long id);
	
	@Query(value = "SELECT * FROM agendamentos where id_paciente = ?1", nativeQuery = true)
	Page<Agendamento> findAgendamentosByIdPaciente(Long id, Pageable pageable);
	
	//Retorna dados que serão utilizados para criar uma tabela personalizada via PROJECTION
	
	@Query("select a.id as id,"
			+ "a.paciente as paciente,"
			+ "CONCAT(a.dataConsulta, ' ', a.horario.horaMinuto) as dataConsulta,"
			+ "a.medico as medico,"
			+ "a.especialidade as especialidade "
			+ "from Agendamento a "
			+ "where a.paciente.usuario.email like :email")
	Page<HistoricoPaciente> findHistoricoByPacienteEmail(String email, Pageable pageable);
	
	//Retorna dados que serão utilizados para criar uma tabela personalizada via PROJECTION
	
	@Query("select a.id as id,"
			+ "a.paciente as paciente,"
			+ "CONCAT(a.dataConsulta, ' ', a.horario.horaMinuto) as dataConsulta,"
			+ "a.medico as medico,"
			+ "a.especialidade as especialidade "
			+ "from Agendamento a "
			+ "where a.medico.usuario.email like :email")
	Page<HistoricoPaciente> findHistoricoByMedicoEmail(String email, Pageable pageable);
	
	/*
	 * Sabemos que tanto o medico quanto o paciente podem alterar consultas relacionadas aos mesmos. Então estamos
	 * verificando se o paciente ou medico estão tentando alterar uma consulta relacionada aos mesmos. Verificar se
	 * a url foi alterada.
	 * 
	 * Primeiro ele busca o agendamento pelo id, depois verifica se o email do usuario presente no
	 * paciente do agendamento é igual ao email passado. O email passado é o email do User logado no
	 * sistema.
	 * 
	 */
	@Query("select a from Agendamento a "
			+ "where "
			+ " (a.id = :id AND a.paciente.usuario.email like :email) "
			+ " OR "
			+ " (a.id = :id AND a.medico.usuario.email like :email)")
	Optional<Agendamento> findByIdAndPacienteOrMedicoEmail(Long id, String email);
	
	
	
	
	
	
	
	
	
	
	
}
