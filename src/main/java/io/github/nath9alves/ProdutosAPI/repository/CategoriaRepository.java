package io.github.nath9alves.ProdutosAPI.repository;

import io.github.nath9alves.ProdutosAPI.model.Categoria;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;



@Repository
public interface CategoriaRepository extends JpaRepository<Categoria, Long> {
    Optional<Categoria> findByNome(String nome);
   

    
    @Query("SELECT c FROM Categoria c WHERE c.nome = :nome AND c.id <> :id")
    Optional<Categoria> findByNomeAndIdNot(@Param("nome") String nome, @Param("id") Long id);
}
   