package io.github.nath9alves.ProdutosAPI.controller;

import java.util.List;

import org.slf4j.Logger;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.*;

import io.github.nath9alves.ProdutosAPI.dto.ProdutoDTO;
import io.github.nath9alves.ProdutosAPI.dto.ProdutoResponseDTO;
import io.github.nath9alves.ProdutosAPI.dto.QuantidadeEstoqueCategoriaDTO;
import io.github.nath9alves.ProdutosAPI.model.Produto;
import io.github.nath9alves.ProdutosAPI.service.ProdutoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
//import lombok.extern.slf4j.Slf4j;

//@Slf4j
@RestController
@RequestMapping("/api/produtos")
@RequiredArgsConstructor
public class ProdutoController {

    private final ProdutoService service;
    
    private static final Logger logger = LoggerFactory.getLogger(ProdutoController.class);

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Produto create(@RequestBody @Valid ProdutoDTO dto) {
        return service.create(dto);
    }
    
    @PostMapping("/batch")
    @ResponseStatus(HttpStatus.CREATED)
    public List<Produto> criarProdutosEmLote(@RequestBody List<@Valid ProdutoDTO> produtos) {
        return service.criarEmLote(produtos);
    }


    @GetMapping
    public Page<ProdutoResponseDTO> findAll(Pageable pageable) {
        return service.findAll(pageable);
    }


    @GetMapping("/{id}")
    public Produto buscarPorId(@PathVariable Long id) {
        logger.info("Buscando produto com ID {}", id);
        return service.findById(id);
    }



    @GetMapping("/filter")
    public Page<Produto> findByFilter(@RequestParam(required = false) String nome,
                                      @RequestParam(required = false) String categoria,
                                      Pageable pageable) {
        return service.findByFilter(nome, categoria, pageable);
    }

    @GetMapping("/estoque-por-categoria") // http://localhost:8080/api/produtos/estoque-por-categoria
    public List<QuantidadeEstoqueCategoriaDTO> buscarEstoquePorCategoria() {
        return service.getQuantidadeEstoqueByCategoria();
    }
    
    @PutMapping("/{id}")   //http://localhost:8080/api/produtos/{id}
    public Produto update(@PathVariable Long id, @RequestBody @Valid ProdutoDTO dto) {
        return service.update(id, dto);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }
    

}
