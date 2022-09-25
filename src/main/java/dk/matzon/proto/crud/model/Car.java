package dk.matzon.proto.crud.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.time.OffsetDateTime;
import java.util.Objects;

@Entity
public class Car {
    @Id
    @Column(nullable = false)
    @Size(min = 17, max = 17, message = "{vin.size}")
    @NotBlank(message = "{vin.notblank}")
    private String vin;

    @Column(nullable = false)
    @NotBlank(message = "{make.notblank}")
    private String make;

    @Column(nullable = false)
    @NotBlank(message = "{model.notblank}")
    private String model;

    @Column(name = "createDate", updatable = false)
    @CreationTimestamp
    private OffsetDateTime createDateTime;

    @Column(name = "updateDate")
    @UpdateTimestamp
    private OffsetDateTime updateDateTime;

    private int mileage;

    public Car() {
    }

    public Car(String vin, String make, String model, int mileage) {
        this.vin = vin;
        this.make = make;
        this.model = model;
        this.mileage = mileage;
    }

    public Car updateFrom(Car _other) {
        this.make = _other.getMake();
        this.model = _other.getModel();
        this.mileage = _other.getMileage();
        return this;
    }

    public String getVin() {
        return vin;
    }

    public void setVin(String vin) {
        this.vin = vin;
    }

    public String getMake() {
        return make;
    }

    public void setMake(String make) {
        this.make = make;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public int getMileage() {
        return mileage;
    }

    public void setMileage(int mileage) {
        this.mileage = mileage;
    }

    @JsonIgnore
    public OffsetDateTime getCreateDateTime() {
        return createDateTime;
    }

    @JsonIgnore
    public OffsetDateTime getUpdateDateTime() {
        return updateDateTime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Car car = (Car) o;
        return Objects.equals(vin, car.vin);
    }

    @Override
    public int hashCode() {
        return Objects.hash(vin);
    }

    @Override
    public String toString() {
        return "Car{" +
                "vin='" + vin + '\'' +
                ", make='" + make + '\'' +
                ", model='" + model + '\'' +
                ", mileage=" + mileage +
                '}';
    }
}
