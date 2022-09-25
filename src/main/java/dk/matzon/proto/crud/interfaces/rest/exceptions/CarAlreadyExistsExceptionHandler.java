package dk.matzon.proto.crud.interfaces.rest.exceptions;

import dk.matzon.proto.crud.model.exceptions.CarAlreadyExistsException;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.annotation.Produces;
import io.micronaut.http.hateoas.JsonError;
import io.micronaut.http.server.exceptions.ExceptionHandler;
import jakarta.inject.Singleton;

@Produces
@Singleton
public class CarAlreadyExistsExceptionHandler implements ExceptionHandler<CarAlreadyExistsException, HttpResponse<?>> {

    @Override
    public HttpResponse<?> handle(HttpRequest request, CarAlreadyExistsException exception) {
        return HttpResponse.badRequest()
                .body(exception.asApiError());
    }
}
