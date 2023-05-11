package ru.yandex.practicum.filmorate.handler;

import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;

@ControllerAdvice
@ResponseBody
@Slf4j
public class ExceptionHandler {

    @Autowired
    private Gson gson;

    @org.springframework.web.bind.annotation.ExceptionHandler(ValidationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String handleValidationException(ValidationException exception) {
        log.error(exception.getMessage());
        return gson.toJson(exception.getMessage());
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(NotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String handleValidationException(NotFoundException exception) {
        log.error(exception.getMessage());
        return gson.toJson(exception.getMessage());
    }
}
