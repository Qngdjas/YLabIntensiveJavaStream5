package ru.qngdjas.habitstracker.api.handler;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import ru.qngdjas.habitstracker.application.dto.message.MultipleMessageDTO;
import ru.qngdjas.habitstracker.application.dto.message.SingleMessageDTO;
import ru.qngdjas.habitstracker.application.utils.validator.ValidationException;
import ru.qngdjas.habitstracker.domain.model.user.EmailException;
import ru.qngdjas.habitstracker.domain.service.core.AlreadyExistsException;
import ru.qngdjas.habitstracker.domain.service.core.IncorrectPasswordException;
import ru.qngdjas.habitstracker.domain.service.core.NotFoundException;
import ru.qngdjas.habitstracker.domain.service.core.RootlessException;

/**
 * Общий обработчик ошибок выполнения контроллеров.
 */
@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    /**
     * Метод обработки неверно введенного email.
     *
     * @param exception Ошибка некорректности введенного email.
     * @return Сообщение об ошибке.
     */
    @ExceptionHandler(EmailException.class)
    public ResponseEntity<SingleMessageDTO> handleEmailIncorrect(EmailException exception) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new SingleMessageDTO(exception.getMessage()));
    }

    /**
     * Метод обработки неверно введенного пароля.
     *
     * @param exception Ошибка некорректности введенного пароля.
     * @return Сообщение об ошибке.
     */
    @ExceptionHandler(IncorrectPasswordException.class)
    public ResponseEntity<SingleMessageDTO> handleIncorrectPassword(IncorrectPasswordException exception) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new SingleMessageDTO(exception.getMessage()));
    }

    /**
     * Метод обработки ограничения прав доступа.
     *
     * @param exception Ошибка ограничения прав доступа.
     * @return Сообщение об ошибке.
     */
    @ExceptionHandler(RootlessException.class)
    public ResponseEntity<SingleMessageDTO> handleRootless(RootlessException exception) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new SingleMessageDTO(exception.getMessage()));
    }

    /**
     * Метод обработки ненайденного объекта.
     *
     * @param exception Ошибка несуществующего объекта.
     * @return Сообщение об ошибке.
     */
    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<SingleMessageDTO> handleNotFound(NotFoundException exception) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new SingleMessageDTO(exception.getMessage()));
    }

    /**
     * Метод обработки неверно введенного пароля.
     *
     * @param exception Ошибка некорректности введенного пароля.
     * @return Сообщение об ошибке.
     */
    @ExceptionHandler(AlreadyExistsException.class)
    public ResponseEntity<SingleMessageDTO> handleAlreadyExists(AlreadyExistsException exception) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(new SingleMessageDTO(exception.getMessage()));
    }

    /**
     * Метод обработки валидации.
     *
     * @param exception Ошибки валидации.
     * @return Сообщение об ошибке.
     */
    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<MultipleMessageDTO> handleValidation(ValidationException exception) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new MultipleMessageDTO(exception.getErrors()));
    }

    /**
     * Метод обработки корректности id контроллеров.
     *
     * @param exception Ошибки преобразования в число.
     * @return Сообщение об ошибке.
     */
    @ExceptionHandler(NumberFormatException.class)
    public ResponseEntity<SingleMessageDTO> handleNumberFormat(NumberFormatException exception) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new SingleMessageDTO("Некорректный id"));
    }
}
