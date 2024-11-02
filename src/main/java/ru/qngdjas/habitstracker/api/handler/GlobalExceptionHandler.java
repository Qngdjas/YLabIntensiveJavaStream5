package ru.qngdjas.habitstracker.api.handler;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import ru.qngdjas.habitstracker.application.dto.message.SingleMessageDTO;
import ru.qngdjas.habitstracker.domain.service.core.IncorrectPasswordException;
import ru.qngdjas.habitstracker.domain.service.core.NotFoundException;

@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(IncorrectPasswordException.class)
    public ResponseEntity<SingleMessageDTO> handleIncorrectPassword(IncorrectPasswordException exception) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new SingleMessageDTO(exception.getMessage()));
    }

    @ExceptionHandler
    public ResponseEntity<SingleMessageDTO> handleNotFound(NotFoundException exception) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new SingleMessageDTO(exception.getMessage()));
    }
}
