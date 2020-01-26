package com.allissonjardel.projeto.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.allissonjardel.projeto.model.Usuario;

public interface UsuarioRepository extends JpaRepository<Usuario, Long>{
	
	@Query(value = "select * from usuarios u where u.email like ?1", nativeQuery=true)
	Usuario findByEmail(String email);
	
	@Query(value = "select * from usuarios u " + 
			"inner join usuarios_tem_perfis utp on u.id = utp.usuario_id " + 
			"inner join perfis p on p.id = utp.perfil_id where p.descricao = ?1 or u.email like concat('%', ?1, '%')",
			nativeQuery=true)		
	Page<Usuario> findByEmailOrPerfil(String search, Pageable pageable);

	@Query(value = "select * from usuarios u " + 
			"inner join usuarios_tem_perfis utp on u.id = utp.usuario_id " + 
			"inner join perfis p on p.id = utp.perfil_id where u.id = ?1 AND p.id IN ?2",
			nativeQuery=true)	
	Optional<Usuario> findByIdAndPerfis(Long id, Long[] perfisId);

	@Query(value = "select id from usuarios u where u.email like ?1", nativeQuery=true)
	Long getById(String email);
	
	@Query("select u from Usuario u where u.email like :email and u.ativo = true")
	Optional<Usuario> findByEmailAndAtivo(String email);
	
}
