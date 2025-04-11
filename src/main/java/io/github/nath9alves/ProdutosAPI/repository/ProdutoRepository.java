package io.github.nath9alves.ProdutosAPI.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import io.github.nath9alves.ProdutosAPI.dto.QuantidadeEstoqueCategoriaDTO;
import io.github.nath9alves.ProdutosAPI.model.Produto;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProdutoRepository extends JpaRepository<Produto, Long> {

    boolean existsByNomeAndCategoriaId(String nome, Long categoriaId);

    Optional<Produto> findByIdAndQuantidadeEstoqueGreaterThan(Long id, Integer quantidadeEstoque);

    Page<Produto> findAll(Pageable pageable);

    @Query("SELECT p FROM Produto p WHERE " +
           "(:nome IS NULL OR p.nome LIKE %:nome%) AND " +
           "(:categoriaNome IS NULL OR p.categoria.nome LIKE %:categoriaNome%)")
    Page<Produto> findByNomeAndCategoria(String nome, String categoriaNome, Pageable pageable);

    @Query("SELECT new io.github.nath9alves.ProdutosAPI.dto.QuantidadeEstoqueCategoriaDTO(c.nome, SUM(p.quantidadeEstoque)) " +
    	       "FROM Produto p JOIN p.categoria c GROUP BY c.nome")
    	List<QuantidadeEstoqueCategoriaDTO> countTotalProdutosByCategoria();


    boolean existsByNomeAndCategoriaIdAndIdNot(String nome, Long categoriaId, Long id);
}