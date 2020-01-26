package com.allissonjardel.projeto.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import com.allissonjardel.projeto.model.Medico;

public interface MedicoRepository extends JpaRepository<Medico, Long>{

	@Query(value = "select * from medicos m inner join usuarios u on m.id_usuario = u.id where u.id = ?1", 
			nativeQuery = true)
	Optional<Medico> findByUsuarioId(Long id);
	
	@Query(value = "select m.id from usuarios u inner join medicos m on u.id = m.id_usuario " + 
			"where u.email like ?1 ", nativeQuery = true)
	Long findIdbyUsername(String username);
	
	@Modifying
    @Transactional 
	@Query(value = "delete from medicos_tem_especialidades where id_medico = ?1 and id_especialidade = ?2", 
			nativeQuery = true)
	void removeSpecialization(Long idMedico, Long idEspecialidade);
	
	@Query(value = "select * from medicos m " + 
			"inner join medicos_tem_especialidades mte on mte.id_medico = m.id " + 
			"inner join especialidades e on e.id = mte.id_especialidade where e.titulo = ?1", nativeQuery = true)
	List<Medico> findMedicosByEspeciality(String titulo);

	@Query("select m.id "
			+ "from Medico m "
			+ "join m.especialidades e "
			+ "join m.agendamentos a "
			+ "where "
			+ "a.especialidade.id = :idEsp AND a.medico.id = :idMed")
	Optional<Long> hasEspecialidadeAgendada(Long idMed, Long idEsp);

}
	

