package pl.mzuchnik.springpracadomowa4.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import pl.mzuchnik.springpracadomowa4.model.Car;
import pl.mzuchnik.springpracadomowa4.service.CarService;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/cars")
public class CarController {

    private CarService carService;

    public CarController(CarService carService) {
        this.carService = carService;
    }

    @GetMapping()
    public String getCarAllCars(@RequestParam(name = "info",required = false) String info, Model model){
        List<Car> allCars = carService.getAllCars();
        long id = 0;
        model.addAttribute("info",info);
        model.addAttribute("id", id);
        model.addAttribute("car", new Car());
        model.addAttribute("cars", allCars);
        model.addAttribute("text", "Lista samochodów ("+allCars.size()+")");
        return "all-cars";
    }

    @GetMapping("/{id}")
    public String getCarById(@PathVariable long id, Model model) {
        model.addAttribute("id", id);
        model.addAttribute("car", new Car());
        Optional<Car> carById = carService.getCarById(id);
        carById.ifPresentOrElse(car -> {
            List<Car> cars = Arrays.asList(car);
            model.addAttribute("info", "Samochód o id: " + id);
            model.addAttribute("cars", cars);
        },() ->
        {
            model.addAttribute("info", "Brak pojazdu o id: " + id);
            model.addAttribute("cars", null);
        });
        return "all-cars";
    }

    @PostMapping("/findCar")
    public String findCarById(@RequestParam(value = "id") long id) {
        return "redirect:/cars/" + id;
    }

    @PostMapping("/remove")
    public String findCarToRemove(@RequestParam(value = "id") long id, RedirectAttributes ra)
    {
        Optional<Car> carById = carService.getCarById(id);
        ra.addAttribute("id",id);
        carById.ifPresentOrElse(car -> {
            ra.addAttribute("info","Usunięto pojazd o id: "+id);
            carService.removeCarById(id);
        }, () -> {
            ra.addAttribute("info","Brak pojazdu o id: "+id);
        });
        return "redirect:/cars/";
    }

    @PostMapping("/add")
    public String addCar(@ModelAttribute Car car, RedirectAttributes ra)
    {
        long id = car.getId();
        long containRepeatId = carService.getAllCars().stream()
                .filter(carId -> id == carId.getId())
                .count();
        if (containRepeatId == 0) {
            carService.addCar(car);
            ra.addAttribute("info","Dodano nowy pojazd o id: " +id);
        }else{
            ra.addAttribute("info","Istnieje już pojazd o id: " + id);
        }

        return "redirect:/cars";
    }
    @PostMapping("/update")
    public String updateCar(@ModelAttribute Car car, RedirectAttributes ra)
    {
        long containRepeatId = carService.getAllCars().stream()
                .filter(carId -> car.getId() == carId.getId())
                .count();
        if (containRepeatId == 0) {
            ra.addAttribute("info","Brak pojazu o id: " + car.getId());
        }else{
            Car carById = carService.getCarById(car.getId()).get();
            carById.setColor(car.getColor());
            carById.setMark(car.getMark());
            carById.setModel(car.getModel());
            ra.addAttribute("info","Zaktualizowano pojazd o id: " + car.getId());
        }
        return "redirect:/cars";
    }



}
