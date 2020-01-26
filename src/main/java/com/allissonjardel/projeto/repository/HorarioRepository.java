package com.allissonjardel.projeto.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.allissonjardel.projeto.model.Horario;

public interface HorarioRepository extends JpaRepository<Horario, Long>{

	
	@Query(value = "select * from horario h where not exists(" + 
	" select * from agendamentos a " + 
	" inner join horario hora on hora.id = a.id_horario" + 
	" inner join medicos m on m.id = a.id_medico" + 
	" where m.id = ?1 and a.data_consulta = ?2 and hora.id = h.id) order by h.hora_minuto asc", nativeQuery = true)
	List<Horario> findByMedicoIdAndDataNotHorarioAgendado(Long id, LocalDate data);	

}
