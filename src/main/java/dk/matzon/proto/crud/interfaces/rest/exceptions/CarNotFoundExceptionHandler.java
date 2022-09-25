package dk.matzon.proto.crud.interfaces.rest.exceptions;

import dk.matzon.proto.crud.model.exceptions.CarNotFoundException;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.Produces;
import io.micronaut.http.server.exceptions.ExceptionHandler;
import jakarta.inject.Singleton;

@Produces
@Singleton
public class CarNotFoundExceptionHandler implements ExceptionHandler<CarNotFoundException, HttpResponse<?>> {

    @Override
    public HttpResponse<?> handle(HttpRequest request, CarNotFoundException exception) {
        return HttpResponse.notFound().body(exception.asApiError());
    }
}
