package com.allissonjardel.projeto.repository;

import java.util.List;
import java.util.Set;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.allissonjardel.projeto.model.Especialidade;

public interface EspecialidadeRepository extends JpaRepository<Especialidade, Long>{

	// Retorna todos os tipos de especialidade
	@Query(value = "select * from especialidades where titulo LIKE concat('%', ?1, '%')", nativeQuery = true)
	Page<Especialidade> findAllByTitulo(String search, Pageable pageable);
	
	// Retorna todas as especialidades que contenham esse termo presente em seu titulo
	@Query(value = "select titulo from especialidades where titulo LIKE concat('%', ?1, '%')", nativeQuery = true)
	List<String> findEspecialidadeByTermo(String search);
	
	// Seleciona especialidade que tenha determinado titulo
	@Query(value = "select * from especialidades where titulo LIKE ?1", nativeQuery = true)
	Especialidade findByTitulo(String search);

	// Buscar especialidade por titulos
	@Query(value = "select * from especialidades where titulo IN ?1", nativeQuery = true)
	Set<Especialidade> findByTitulos(String[] titulos);
	
	// Especialidades de um médico
	@Query(value = "select * from especialidades as e inner join "
			+ "medicos_tem_especialidades mte on mte.id_especialidade = e.id where mte.id_medico = ?1", nativeQuery = true)
	Page<Especialidade> getEspecialidadesBy(Long id, Pageable pageable);
	
}
