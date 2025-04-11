package io.github.nath9alves.ProdutosAPI.dto;

import jakarta.validation.constraints.*;
import java.math.BigDecimal;

public record ProdutoDTO(
        Long id,
        @NotBlank(message = "Nome do produto é obrigatório!") String nome,
        String descricao,
        @NotNull(message = "Preço do produto é obrigatório!")
        @DecimalMin(value = "0.01", message = "Preço deve ser maior que zero!")
        BigDecimal preco,
        @NotNull(message = "É obrigatório informar a quantidade disponível em estoque!")
        @Min(value = 0, message = "Quantidade em estoque não pode ser negativa!")
        Integer quantidadeEstoque,
        @NotNull(message = "Categoria é obrigatória!") Long categoriaId
) {}