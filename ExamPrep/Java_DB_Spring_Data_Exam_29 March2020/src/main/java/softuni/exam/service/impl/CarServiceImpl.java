package softuni.exam.service.impl;

import com.google.gson.Gson;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import softuni.exam.models.dto.ImportCarDTO;
import softuni.exam.models.entity.Car;
import softuni.exam.repository.CarRepository;
import softuni.exam.service.CarService;
import softuni.exam.util.ValidationUtil;

import javax.transaction.Transactional;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CarServiceImpl implements CarService {
    public static final Path CARS_FILE_PATH
            = Path.of("src", "main", "resources", "files", "json", "cars.json");

    private final CarRepository carRepository;
    private final Gson gson;
    private final ModelMapper mapper;
    private final ValidationUtil validationUtil;

    @Autowired
    public CarServiceImpl(CarRepository carRepository, Gson gson, ModelMapper mapper, ValidationUtil validationUtil) {
        this.carRepository = carRepository;
        this.gson = gson;
        this.mapper = mapper;
        this.validationUtil = validationUtil;
    }

    @Override
    public boolean areImported() {
        return this.carRepository.count() > 0;
    }

    @Override
    public String readCarsFileContent() throws IOException {
        return Files.readString(CARS_FILE_PATH);
    }

    @Override
    public String importCars() throws IOException {
        ImportCarDTO[] importCarDTOS = this.gson.fromJson(readCarsFileContent(), ImportCarDTO[].class);

        return Arrays.stream(importCarDTOS)
                .map(this::importValidCar)
                .collect(Collectors.joining(System.lineSeparator()));
    }

    private String importValidCar(ImportCarDTO dto) {
        boolean isValid = this.validationUtil.isValid(dto);

        if (isValid) {
            Car car = this.mapper.map(dto, Car.class);
            this.carRepository.save(car);

            return String.format("Successfully imported car - %s - %s", car.getMake(), car.getModel());
        }

        return "Invalid car";
    }

    @Override
    @Transactional
    public String getCarsOrderByPicturesCountThenByMake() {
        List<Car> carList = this.carRepository.findAllOrderByPicturesDescMakeAsc();
        return carList
                .stream()
                .map(Car::toString)
                .collect(Collectors.joining(System.lineSeparator()));
    }
}
