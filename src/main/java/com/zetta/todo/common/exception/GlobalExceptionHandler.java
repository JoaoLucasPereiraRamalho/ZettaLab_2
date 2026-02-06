package com.zetta.todo.common.exception;

import com.zetta.todo.common.dto.ErrorResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@RestControllerAdvice
@RequiredArgsConstructor // Injeta o MessageSource automaticamente
public class GlobalExceptionHandler {

    private final MessageSource messageSource;

    // 1. Erros de Validação (@Valid)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponseDTO> handleValidationErrors(MethodArgumentNotValidException ex) {
        List<ErrorResponseDTO.ValidationError> errors = new ArrayList<>();

        for (FieldError error : ex.getBindingResult().getFieldErrors()) {
            // Tenta traduzir a mensagem. Se não conseguir, usa a mensagem padrão.
            String message = messageSource.getMessage(error, LocaleContextHolder.getLocale());
            errors.add(new ErrorResponseDTO.ValidationError(error.getField(), message));
        }

        String title = messageSource.getMessage("error.validation.title", null, LocaleContextHolder.getLocale());
        String detail = messageSource.getMessage("error.validation.detail", null, LocaleContextHolder.getLocale());

        ErrorResponseDTO response = new ErrorResponseDTO(
                title,
                HttpStatus.BAD_REQUEST.value(),
                detail,
                LocalDateTime.now(),
                errors);

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    // 2. Erros de Negócio (Nossa BusinessException)
    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ErrorResponseDTO> handleBusinessException(BusinessException ex) {
        HttpStatus status = HttpStatus.BAD_REQUEST;

        if (ex.getKey().contains("not.found")) {
            status = HttpStatus.NOT_FOUND;
        } else if (ex.getKey().contains("access.denied")) {
            status = HttpStatus.FORBIDDEN;
        } else if (ex.getKey().contains("invalid.credentials")) {
            status = HttpStatus.UNAUTHORIZED;
        }

        // TRADUÇÃO AQUI:
        String message;
        try {
            message = messageSource.getMessage(ex.getKey(), ex.getArgs(), LocaleContextHolder.getLocale());
        } catch (Exception e) {
            message = ex.getKey(); // Se falhar a tradução, devolve a chave mesmo
        }

        ErrorResponseDTO response = new ErrorResponseDTO(
                status.getReasonPhrase(),
                status.value(),
                message, // Mensagem traduzida!
                LocalDateTime.now(),
                null);

        return ResponseEntity.status(status).body(response);
    }

    // 3. Erros Genéricos
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponseDTO> handleGenericErrors(Exception ex) {
        ex.printStackTrace();

        String message = messageSource.getMessage("error.generic", null, LocaleContextHolder.getLocale());
        String title = messageSource.getMessage("error.internal.title", null, LocaleContextHolder.getLocale());

        ErrorResponseDTO response = new ErrorResponseDTO(
                title,
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                message,
                LocalDateTime.now(),
                null);

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }
}