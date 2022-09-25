package dk.matzon.proto.crud.application.persistence;

import dk.matzon.proto.crud.model.Car;
import io.micronaut.data.annotation.Repository;
import io.micronaut.data.repository.CrudRepository;

@Repository
public interface CarRepository extends CrudRepository<Car, String> {
}
