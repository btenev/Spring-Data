package softuni.exam.service.impl;

import com.google.gson.Gson;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import softuni.exam.models.dtos.ImportTownDTO;
import softuni.exam.models.entities.Town;
import softuni.exam.repository.TownRepository;
import softuni.exam.service.TownService;
import softuni.exam.util.ValidationUtil;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.stream.Collectors;

@Service
public class TownServiceImpl implements TownService {
    private static final Path TOWNS_FILE_PATH
            = Path.of("src", "main", "resources", "files", "json", "towns.json");

    private final TownRepository townRepository;
    private final Gson gson;
    private final ModelMapper mapper;
    private final ValidationUtil validationUtil;

    @Autowired
    public TownServiceImpl(TownRepository townRepository, Gson gson, ModelMapper mapper, ValidationUtil validationUtil) {
        this.townRepository = townRepository;
        this.gson = gson;
        this.mapper = mapper;
        this.validationUtil = validationUtil;
    }

    @Override
    public boolean areImported() {
        return this.townRepository.count() > 0;
    }

    @Override
    public String readTownsFileContent() throws IOException {
        return Files.readString(TOWNS_FILE_PATH);
    }

    @Override
    public String importTowns() throws IOException {
        ImportTownDTO[] importTownDTOS = this.gson.fromJson(readTownsFileContent(), ImportTownDTO[].class);

        return Arrays.stream(importTownDTOS)
                .map(this::importValidTown)
                .collect(Collectors.joining(System.lineSeparator()));
    }

    private String importValidTown(ImportTownDTO dto) {
        boolean isValid = this.validationUtil.isValid(dto);

        if (isValid) {
            Town town = this.mapper.map(dto, Town.class);

            this.townRepository.save(town);

            return String.format("Successfully imported Town %s - %d", town.getName(), town.getPopulation());
        }

        return "Invalid Town";
    }
}
