package ru.agile.scrum.mst.market.auth.exceptions;

import lombok.Getter;
import ru.agile.scrum.mst.market.api.ListResponse;
import ru.agile.scrum.mst.market.api.ValidationError;

@Getter
public class JsonUserFormValidationErrorException extends RuntimeException {

    private final ListResponse<ValidationError> errors;

    public JsonUserFormValidationErrorException(ListResponse<ValidationError> errors) {
        this.errors = errors;
    }
}
