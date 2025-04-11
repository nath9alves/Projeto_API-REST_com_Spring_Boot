package io.github.nath9alves.ProdutosAPI.service;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.github.nath9alves.ProdutosAPI.dto.ProdutoDTO;
import io.github.nath9alves.ProdutosAPI.dto.ProdutoResponseDTO;
import io.github.nath9alves.ProdutosAPI.dto.QuantidadeEstoqueCategoriaDTO;
import io.github.nath9alves.ProdutosAPI.exception.BusinessException;
import io.github.nath9alves.ProdutosAPI.exception.EntityNotFoundException;
import io.github.nath9alves.ProdutosAPI.model.Categoria;
import io.github.nath9alves.ProdutosAPI.model.Produto;
import io.github.nath9alves.ProdutosAPI.repository.CategoriaRepository;
import io.github.nath9alves.ProdutosAPI.repository.ProdutoRepository;
import jakarta.validation.Valid;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProdutoService {
    private static final Logger logger = LoggerFactory.getLogger(ProdutoService.class);
    private final ProdutoRepository repository;
    private final CategoriaRepository categoriaRepository;


    @Transactional
    public Produto create(ProdutoDTO dto) {
        logger.info("Criando produto: {}", dto.nome());
        
        if (repository.existsByNomeAndCategoriaId(dto.nome(), dto.categoriaId())) {
            throw new BusinessException("Já existe um produto com este nome na mesma categoria");
        }
        
        Categoria categoria = categoriaRepository.findById(dto.categoriaId())
                .orElseThrow(() -> new EntityNotFoundException("Categoria não encontrada"));
        
        Produto produto = new Produto();
        produto.setNome(dto.nome());
        produto.setDescricao(dto.descricao());
        produto.setPreco(dto.preco());
        produto.setQuantidadeEstoque(dto.quantidadeEstoque());
        produto.setCategoria(categoria);
        
        return repository.save(produto);
    }
    
    public ProdutoResponseDTO toResponseDTO(Produto produto) {
        return new ProdutoResponseDTO(
            produto.getId(),
            produto.getNome(),
            produto.getDescricao(),
            produto.getPreco(),
            produto.getQuantidadeEstoque(),
            produto.getCategoria().getNome()
        );
    }


    public Page<ProdutoResponseDTO> findAll(Pageable pageable) {
        logger.info("Listando produtos com DTO");
        return repository.findAll(pageable)
                .map(this::toResponseDTO);
    }


    public Produto findById(Long id) {
        logger.info("Buscando produto por ID: {}", id);
        return repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Produto não encontrado"));
    }

    public Page<Produto> findByFilter(String nome, String categoriaNome, Pageable pageable) {
        logger.info("Filtrando produtos por nome: {} e categoria: {}", nome, categoriaNome);
        return repository.findByNomeAndCategoria(nome, categoriaNome, pageable);
    }

    @Transactional
    public Produto update(Long id, ProdutoDTO dto) {
        logger.info("Atualizando produto ID {}: {}", id, dto.nome());

        Produto produto = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Produto não encontrado"));

        if (repository.existsByNomeAndCategoriaIdAndIdNot(dto.nome(), dto.categoriaId(), id)) {
            throw new BusinessException("Já existe um produto com este nome na mesma categoria");
        }

        Categoria categoria = categoriaRepository.findById(dto.categoriaId())
                .orElseThrow(() -> new EntityNotFoundException("Categoria não encontrada"));

        produto.setNome(dto.nome());
        produto.setDescricao(dto.descricao());
        produto.setPreco(dto.preco());
        produto.setQuantidadeEstoque(dto.quantidadeEstoque());
        produto.setCategoria(categoria);

        return repository.save(produto);
    }


    @Transactional
    public void delete(Long id) {
        logger.info("Deletando produto ID: {}", id);
        
        Produto produto = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Produto não encontrado"));

        if (produto.getQuantidadeEstoque() == 0) {
            throw new BusinessException("Não é possível excluir produto com estoque zerado");
        }

        repository.delete(produto);
        logger.info("Produto ID {} excluído com sucesso!", id);
    }


    public List<QuantidadeEstoqueCategoriaDTO> getQuantidadeEstoqueByCategoria() {
        logger.info("Calculando quantidade total de produtos em estoque por categoria");
        return repository.countTotalProdutosByCategoria();
    }

    @Transactional
    public List<Produto> criarEmLote(List<ProdutoDTO> dtos) {
        return dtos.stream()
            .map(this::create)
            .toList();
    }
    public List<QuantidadeEstoqueCategoriaDTO> getQuantidadeEstoqueByCategoria1() {
        logger.info("Consultando quantidade total de estoque por categoria");
        return repository.countTotalProdutosByCategoria();
    }

}