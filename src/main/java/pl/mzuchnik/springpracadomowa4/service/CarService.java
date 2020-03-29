package pl.mzuchnik.springpracadomowa4.service;

import pl.mzuchnik.springpracadomowa4.model.Car;

import java.util.List;
import java.util.Optional;

public interface CarService {

    List<Car> getAllCars();
    Optional<Car> getCarById(long id);
    List<Car> getCarsByColor(String color);
    Car addCar(Car car);
    Car removeCarById(long id);
}
