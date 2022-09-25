package dk.matzon.proto.crud.application.persistence;

import dk.matzon.proto.crud.model.Car;
import dk.matzon.proto.crud.model.exceptions.CarAlreadyExistsException;
import dk.matzon.proto.crud.model.exceptions.CarNotFoundException;
import jakarta.inject.Singleton;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Singleton
public class CarService {
    private final CarRepository carRepository;

    public CarService(CarRepository carRepository) {
        this.carRepository = carRepository;
    }

    public Car addCar(final Car _car) {

        if (carRepository.existsById(_car.getVin())) {
            throw new CarAlreadyExistsException(_car.getVin());
        }

        return carRepository.save(_car);
    }

    public List<Car> getCars() {
        Iterable<Car> iterable = carRepository.findAll();
        return StreamSupport.stream(iterable.spliterator(), false)
                .collect(Collectors.toList());
    }

    public Car getCar(final String _vin) {
        return carRepository.findById(_vin)
                .orElseThrow(() -> new CarNotFoundException(_vin));
    }

    public Car updateCar(String vin, final Car _car) {
        return carRepository.findById(vin)
                .map(car -> {
                    car.updateFrom(_car);
                    return carRepository.update(car);
                }).orElseGet(() -> {
                    _car.setVin(vin);
                    return carRepository.save(_car);
                });
    }

    public void deleteCar(final String _vin) {
        carRepository.findById(_vin)
                .map((car) -> {
                    carRepository.delete(car);
                    return car;
                })
                .orElseThrow(() -> new CarNotFoundException(_vin));
    }
}
