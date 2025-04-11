package io.github.nath9alves.ProdutosAPI.exception;

public class BusinessException extends RuntimeException {
    public BusinessException(String message) {
        super(message);
    }
}