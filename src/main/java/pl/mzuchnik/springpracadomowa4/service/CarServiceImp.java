package pl.mzuchnik.springpracadomowa4.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.mzuchnik.springpracadomowa4.exception.CarNotFoundException;
import pl.mzuchnik.springpracadomowa4.model.Car;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CarServiceImp implements CarService {

    private List<Car> carList;

    @Autowired
    public CarServiceImp(List<Car> carList) {
        this.carList = carList;
    }

    @Override
    public List<Car> getAllCars() {
        return carList;
    }

    @Override
    public Optional<Car> getCarById(long id) {
        return carList.stream()
                .filter(car -> car.getId() == id)
                .findFirst();
    }

    @Override
    public List<Car> getCarsByColor(String color) {
        return carList.stream()
                .filter(car -> car.getColor().equalsIgnoreCase(color))
                .collect(Collectors.toList());
    }

    @Override
    public Car addCar(Car car) {
        carList.add(car);
        return car;
    }

    @Override
    public Car removeCarById(long id) {
        Car removedCar = carList.stream()
                .filter(car -> car.getId() == id)
                .findFirst()
                .orElseThrow(() -> new CarNotFoundException(id));
        carList.remove(removedCar);
        return removedCar;
    }
}
