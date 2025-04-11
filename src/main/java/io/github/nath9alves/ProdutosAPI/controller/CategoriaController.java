package io.github.nath9alves.ProdutosAPI.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import io.github.nath9alves.ProdutosAPI.dto.CategoriaDTO;
import io.github.nath9alves.ProdutosAPI.dto.CategoriaResponseDTO;
import io.github.nath9alves.ProdutosAPI.model.Categoria;
import io.github.nath9alves.ProdutosAPI.service.CategoriaService;

import java.util.List;

@RestController
@RequestMapping("/api/categorias")
@RequiredArgsConstructor
public class CategoriaController {

    private final CategoriaService service;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Categoria create(@RequestBody @Valid CategoriaDTO dto) {
        return service.create(dto);
    }
    
    @PostMapping("/batch")    // http://localhost:8080/api/categorias/batch
    @ResponseStatus(HttpStatus.CREATED)
    public List<Categoria> criarVariasCategorias(@RequestBody List<@Valid CategoriaDTO> categorias) {
        return categorias.stream()
                .map(service::create)
                .toList();
    }


    @GetMapping
    public List<CategoriaResponseDTO> listarCategorias() {
        return service.findAll();
    }


    @GetMapping("/{id}")
    public Categoria findById(@PathVariable Long id) {
        return service.findById(id);
    }

    @PutMapping("/{id}")
    public Categoria update(@PathVariable Long id, @RequestBody @Valid CategoriaDTO dto) {
        return service.update(id, dto);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }
}
