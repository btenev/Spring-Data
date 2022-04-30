package softuni.exam.instagraphlite.service.impl;

import com.google.gson.Gson;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import softuni.exam.instagraphlite.models.dtos.ImportPictureDTO;
import softuni.exam.instagraphlite.models.entities.Picture;
import softuni.exam.instagraphlite.repository.PictureRepository;
import softuni.exam.instagraphlite.service.PictureService;
import softuni.exam.instagraphlite.util.ValidationUtil;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PictureServiceImpl implements PictureService {
    public static final Path PICTURES_FILE_PATH
            = Path.of("src", "main", "resources", "files", "pictures.json");

    private final PictureRepository pictureRepository;
    private final Gson gson;
    private final ValidationUtil validationUtil;
    private final ModelMapper mapper;

    @Autowired
    public PictureServiceImpl(PictureRepository pictureRepository, Gson gson, ValidationUtil validationUtil,
                              ModelMapper mapper) {
        this.pictureRepository = pictureRepository;
        this.gson = gson;
        this.validationUtil = validationUtil;
        this.mapper = mapper;
    }

    @Override
    public boolean areImported() {
        return this.pictureRepository.count() > 0;
    }

    @Override
    public String readFromFileContent() throws IOException {
        return Files.readString(PICTURES_FILE_PATH);
    }

    @Override
    public String importPictures() throws IOException {
        ImportPictureDTO[] importPictureDTOS = gson.fromJson(readFromFileContent(), ImportPictureDTO[].class);

        return Arrays.stream(importPictureDTOS)
                .map(this::importValidPicture)
                .collect(Collectors.joining(System.lineSeparator()));
    }

    private String importValidPicture(ImportPictureDTO dto) {
        boolean isValid = validationUtil.isValid(dto);
        boolean isEntityAvailable = this.pictureRepository.existsByPath(dto.getPath());

        if (isValid && !isEntityAvailable) {
            Picture picture = this.mapper.map(dto, Picture.class);

            this.pictureRepository.save(picture);

            return String.format("Successfully imported Picture, with size %.2f", picture.getSize());
        }

        return "Invalid Picture";
    }

    @Override
    public String exportPictures() {
        List<Picture> pictures = this.pictureRepository.findAllBySizeGreaterThanOrderBySize(30000.00);

        return pictures
                .stream()
                .map(Picture::toString)
                .collect(Collectors.joining(System.lineSeparator()));
    }
}
