package com.codingee.ranked.ranktracker.util.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class UniqueConstraintException extends RuntimeException {

    public UniqueConstraintException(String message) {
        super(message);
    }
}
