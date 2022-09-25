package dk.matzon.proto.crud.model.exceptions;

import dk.matzon.proto.crud.interfaces.rest.exceptions.ApiError;
import io.micronaut.http.HttpStatus;

public class CarNotFoundException extends RuntimeException {
    private String vin;

    public CarNotFoundException(String vin) {
        this.vin = vin;
    }

    public String getVin() {
        return vin;
    }

    public ApiError asApiError() {
        return new ApiError(HttpStatus.NOT_FOUND, "Car not found", "No such Car found by VIN: " + vin);
    }
}
