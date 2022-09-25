package dk.matzon.proto.crud.interfaces.rest;

import dk.matzon.proto.crud.application.persistence.CarRepository;
import dk.matzon.proto.crud.model.Car;
import io.micronaut.http.*;
import io.micronaut.http.client.BlockingHttpClient;
import io.micronaut.http.client.HttpClient;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.http.client.exceptions.HttpClientResponseException;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.*;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowableOfType;

@MicronautTest
class CarControllerTest {
    @Inject
    @Client("/")
    private HttpClient client;

    @Inject
    private CarRepository carRepository;

    @AfterEach
    void tearDown() {
        // tests seemingly running transactionally - but still need to delete on teardown -.-
        carRepository.deleteAll();
    }

    @Test
    void testCreate() {
        BlockingHttpClient blockingHttpClient = client.toBlocking();

        Car car = new Car("5FNRL382X7B001853", "Honda", "Odyssey", 1337);
        Car postedCar = addCar(blockingHttpClient, car);

        assertThat(postedCar).isNotNull();
    }

    @Test
    void testCreateConstraintValidation() {
        BlockingHttpClient blockingHttpClient = client.toBlocking();

        Car car = new Car("JH4CC2560RC008414", "Acura", "Vigor", 1337);
        addCar(blockingHttpClient, car);

        HttpClientResponseException exception = catchThrowableOfType(() -> {
            addCar(blockingHttpClient, car);
        }, HttpClientResponseException.class);

        assertThat(exception.getStatus().getCode()).isEqualTo(HttpStatus.BAD_REQUEST.getCode());
    }

    @Test
    void testCreateValidationModelFail() {
        BlockingHttpClient blockingHttpClient = client.toBlocking();

        Car car = new Car("JH4DA9370MS016526", "", "Integra", 1337);
        HttpClientResponseException exception = catchThrowableOfType(() -> {
            addCar(blockingHttpClient, car);
        }, HttpClientResponseException.class);

        assertThat(exception.getStatus().getCode()).isEqualTo(HttpStatus.BAD_REQUEST.getCode());
    }

    @Test
    void testReadEmpty() {
        BlockingHttpClient blockingHttpClient = client.toBlocking();

        List<?> carList = blockingHttpClient.retrieve("/cars", List.class);
        assertThat(carList).isEmpty();
    }

    @Test
    void testReadSpecific() {
        BlockingHttpClient blockingHttpClient = client.toBlocking();

        addCar(blockingHttpClient, new Car("2C4GM68475R667819", "Chrysler", "Pacifica", 1337));

        Car car = blockingHttpClient.retrieve("/cars/2C4GM68475R667819", Car.class);

        assertThat(car).isNotNull();
        assertThat(car.getVin()).isEqualTo("2C4GM68475R667819");
    }

    @Test
    void testReadSpecificMissing() {
        BlockingHttpClient blockingHttpClient = client.toBlocking();

        HttpClientResponseException exception = catchThrowableOfType(() -> {
            blockingHttpClient.exchange("/cars/KNAFB121625150469", Car.class);
        }, HttpClientResponseException.class);

        assertThat(exception.getStatus().getCode()).isEqualTo(HttpStatus.NOT_FOUND.getCode());
    }

    @Test
    void testReadAll() {
        BlockingHttpClient blockingHttpClient = client.toBlocking();

        addCar(blockingHttpClient, new Car("JH4KA7650PC002520", "Acura", "Legend", 1337));
        addCar(blockingHttpClient, new Car("JNKCV51E03M018631", "Infiniti", "G35", 1337));

        List<?> carList = blockingHttpClient.retrieve("/cars", List.class);
        assertThat(carList).hasSize(2);
    }

    @Test
    void testUpdate() {
        BlockingHttpClient blockingHttpClient = client.toBlocking();

        Car car = new Car("1G6CD1184H4323745", "Cadillac", "DeVille", 1337);
        addCar(blockingHttpClient, car);

        car.setMileage(2674);

        Car updatedCar = blockingHttpClient.retrieve(
                HttpRequest.PUT("/cars/1G6CD1184H4323745", car)
                        .contentType(MediaType.APPLICATION_JSON_TYPE), Car.class);

        assertThat(updatedCar.getMileage()).isEqualTo(2674);
    }

    @Test
    void testDelete() {
        BlockingHttpClient blockingHttpClient = client.toBlocking();

        Car car = new Car("1G4AW69N2DH524774", "Buick", "Electra", 1337);
        addCar(blockingHttpClient, car);
        HttpResponse<Void> response = blockingHttpClient.exchange(
                HttpRequest.DELETE("/cars/1G4AW69N2DH524774"));

        assertThat(response.getStatus().getCode()).isEqualTo(HttpStatus.NO_CONTENT.getCode());
    }

    @Test
    void testHead() {
        BlockingHttpClient blockingHttpClient = client.toBlocking();

        Car car = new Car("5N3AA08D68N901917", "Infiniti", "QX56", 1337);
        addCar(blockingHttpClient, car);

        HttpResponse<Void> response = blockingHttpClient.exchange(
                HttpRequest.HEAD("/cars/5N3AA08D68N901917"));

        assertThat(response.getHeaders().findDate(HttpHeaders.LAST_MODIFIED)).isNotEmpty();
    }

    private Car addCar(BlockingHttpClient blockingHttpClient, Car car) {
        return blockingHttpClient.retrieve(
                HttpRequest.POST("/cars", car)
                        .contentType(MediaType.APPLICATION_JSON_TYPE), Car.class);
    }
}