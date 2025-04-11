package io.github.nath9alves.ProdutosAPI.service;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.github.nath9alves.ProdutosAPI.dto.CategoriaDTO;
import io.github.nath9alves.ProdutosAPI.dto.CategoriaResponseDTO;
import io.github.nath9alves.ProdutosAPI.exception.BusinessException;
import io.github.nath9alves.ProdutosAPI.exception.EntityNotFoundException;
import io.github.nath9alves.ProdutosAPI.model.Categoria;
import io.github.nath9alves.ProdutosAPI.repository.CategoriaRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CategoriaService {

    private static final Logger logger = LoggerFactory.getLogger(CategoriaService.class);
    private final CategoriaRepository repository;

    @Transactional
    public Categoria create(CategoriaDTO dto) {
        logger.info("Criando nova categoria: {}", dto.nome());
        validateCategoriaNome(dto.nome());
        Categoria categoria = new Categoria();
        categoria.setNome(dto.nome().trim());
        return repository.save(categoria);
    }

    @Transactional(readOnly = true)
    public List<CategoriaResponseDTO> findAll() {
        logger.debug("Buscando todas as categorias");
        return repository.findAll()
                .stream()
                .map(this::toResponseDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public Categoria findById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Categoria não encontrada com ID: " + id));
    }

    @Transactional
    public Categoria update(Long id, CategoriaDTO dto) {
        Categoria categoria = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Categoria não encontrada com ID: " + id));
        if (!categoria.getNome().equalsIgnoreCase(dto.nome())) {
            validateCategoriaNome(dto.nome());
            categoria.setNome(dto.nome().trim());
        }
        return repository.save(categoria);
    }

    @Transactional
    public void delete(Long id) {
        Categoria categoria = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Categoria não encontrada com ID: " + id));
        if (!categoria.getProdutos().isEmpty()) {
            throw new BusinessException("Não é possível excluir categoria com produtos vinculados");
        }
        repository.delete(categoria);
    }

    private void validateCategoriaNome(String nome) {
        if (nome == null || nome.trim().isEmpty()) {
            throw new BusinessException("Nome da categoria não pode ser vazio");
        }
        repository.findByNome(nome.trim()).ifPresent(c -> {
            throw new BusinessException("Já existe uma categoria com o nome: " + nome);
        });
    }

    private CategoriaResponseDTO toResponseDTO(Categoria categoria) {
        return new CategoriaResponseDTO(categoria.getId(), categoria.getNome());
    }
}
