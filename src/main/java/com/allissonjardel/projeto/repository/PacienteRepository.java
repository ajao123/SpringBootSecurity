package com.allissonjardel.projeto.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.allissonjardel.projeto.model.Paciente;

public interface PacienteRepository extends JpaRepository<Paciente, Long>{

	@Query(value = "select * from pacientes p inner join usuarios u on p.id_usuario = u.id where u.id = ?1", 
			nativeQuery = true)
	Optional<Paciente> findByPacienteId(Long id);
	
	@Query(value = "select p.id from usuarios u inner join pacientes p on u.id = p.id_usuario " + 
			"where u.email like ?1 ", nativeQuery = true)
	Long findIdbyUsername(String username);
	
	@Query(value = "select * from pacientes p inner join usuarios u on u.id = p.id_usuario " + 
			"where u.email like ?1 ", nativeQuery = true)
	Optional<Paciente> findPacientebyUsername(String username);
	
	@Query(value = "SELECT COUNT(*) FROM PACIENTES WHERE NOME = ?1", nativeQuery = true)
	Integer existByName(String nome);
	
}
