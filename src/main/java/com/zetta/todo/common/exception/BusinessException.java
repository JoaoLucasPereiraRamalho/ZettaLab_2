package com.zetta.todo.common.exception;

import lombok.Getter;

@Getter
public class BusinessException extends RuntimeException {

    private final String key;
    private final Object[] args;

    // Construtor só com a chave
    public BusinessException(String key) {
        super(key);
        this.key = key;
        this.args = null;
    }

    // Construtor com chave e argumentos
    public BusinessException(String key, Object... args) {
        super(key);
        this.key = key;
        this.args = args;
    }
}