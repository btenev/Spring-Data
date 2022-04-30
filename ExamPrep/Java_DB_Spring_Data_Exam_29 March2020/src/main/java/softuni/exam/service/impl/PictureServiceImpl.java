package softuni.exam.service.impl;

import com.google.gson.Gson;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import softuni.exam.models.dto.ImportPictureDTO;
import softuni.exam.models.entity.Car;
import softuni.exam.models.entity.Picture;
import softuni.exam.repository.CarRepository;
import softuni.exam.repository.PictureRepository;
import softuni.exam.service.PictureService;
import softuni.exam.util.ValidationUtil;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PictureServiceImpl implements PictureService {
    public static final Path PICTURES_FILE_PATH
            = Path.of("src", "main", "resources", "files", "json", "pictures.json");

    private final PictureRepository pictureRepository;
    private final CarRepository carRepository;
    private final Gson gson;
    private final ValidationUtil validationUtil;
    private final ModelMapper mapper;

    @Autowired
    public PictureServiceImpl(PictureRepository pictureRepository, CarRepository carRepository, Gson gson, ValidationUtil validationUtil, ModelMapper mapper) {
        this.pictureRepository = pictureRepository;
        this.carRepository = carRepository;
        this.gson = gson;
        this.validationUtil = validationUtil;
        this.mapper = mapper;
    }

    @Override
    public boolean areImported() {
        return this.pictureRepository.count() > 0;
    }

    @Override
    public String readPicturesFromFile() throws IOException {
        return Files.readString(PICTURES_FILE_PATH);
    }

    @Override
    public String importPictures() throws IOException {
        ImportPictureDTO[] importPictureDTOS = this.gson.fromJson(readPicturesFromFile(), ImportPictureDTO[].class);

        return Arrays.stream(importPictureDTOS)
                .map(this::importValidPicture)
                .collect(Collectors.joining(System.lineSeparator()));
    }

    private String importValidPicture(ImportPictureDTO dto) {
        boolean isValid = validationUtil.isValid(dto);

        if(isValid) {
            Picture picture = this.mapper.map(dto, Picture.class);

            Optional<Car> optionalCar = this.carRepository.findById(dto.getCar());
            picture.setCar(optionalCar.get());

            this.pictureRepository.save(picture);

            return String.format("Successfully import picture - %s", picture.getName());
        }

        return "Invalid Picture";
    }
}
