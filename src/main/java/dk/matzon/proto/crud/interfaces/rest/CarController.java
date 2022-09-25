package dk.matzon.proto.crud.interfaces.rest;

import dk.matzon.proto.crud.application.persistence.CarService;
import dk.matzon.proto.crud.model.Car;
import io.micronaut.core.util.StringUtils;
import io.micronaut.http.HttpHeaders;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.MutableHttpResponse;
import io.micronaut.http.annotation.*;
import io.micronaut.validation.Validated;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Validated
@Controller(value = "/cars")
public class CarController {
    private final CarService carService;

    public CarController(CarService carService) {
        this.carService = carService;
    }

    @Post
    public HttpResponse<Car> create(@Valid @Body Car car) throws URISyntaxException {
        Car createdCar = carService.addCar(car);
        return HttpResponse.created(createdCar, new URI("/cars/" + createdCar.getVin()));
    }

    @Get
    public List<Car> read() {
        return carService.getCars();
    }

    @Get("/{vin}")
    public HttpResponse<Car> read(String vin) {

        Car car = carService.getCar(vin);

        MutableHttpResponse<Car> response = HttpResponse.ok(car);
        response.header(HttpHeaders.LAST_MODIFIED, DateTimeFormatter.RFC_1123_DATE_TIME.format(car.getUpdateDateTime()));

        return response;
    }

    @Put("/{vin}")
    public HttpResponse<Car> update(String vin, @Body Car car) throws URISyntaxException {
        boolean existingCar = StringUtils.isNotEmpty(car.getVin());
        Car updatedCar = carService.updateCar(vin, car);

        if (existingCar) {
            return HttpResponse.ok(updatedCar);
        } else {
            return HttpResponse.created(updatedCar, new URI("/cars/" + updatedCar.getVin()));
        }
    }

    @Delete("/{vin}")
    public HttpResponse<Void> delete(String vin) {
        carService.deleteCar(vin);
        return HttpResponse.noContent();
    }
}
