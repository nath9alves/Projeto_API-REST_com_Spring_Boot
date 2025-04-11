package io.github.nath9alves.ProdutosAPI.dto;

import java.math.BigDecimal;

public record ProdutoResponseDTO(
    Long id,
    String nome,
    String descricao,
    BigDecimal preco,
    Integer quantidadeEstoque,
    String categoria
) {}
