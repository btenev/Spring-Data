package com.example.football.service.impl;

import com.example.football.models.dto.TownImportDTO;
import com.example.football.models.entity.Town;
import com.example.football.repository.TownRepository;
import com.example.football.service.TownService;
import com.example.football.util.ValidationUtil;
import com.google.gson.Gson;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class TownServiceImpl implements TownService {
    Path TOWN_PATH_FILE
            = Path.of("src", "main", "resources", "files", "json", "towns.json");

    private final TownRepository townRepository;
    private final Gson gson;
    private final ValidationUtil validationUtil;
    private final ModelMapper mapper;

    @Autowired
    public TownServiceImpl(TownRepository townRepository, Gson gson, ValidationUtil validationUtil, ModelMapper mapper) {
        this.townRepository = townRepository;

        this.validationUtil = validationUtil;
        this.gson = gson;
        this.mapper = mapper;

    }

    @Override
    public boolean areImported() {
        return townRepository.count() > 0;
    }

    @Override
    public String readTownsFileContent() throws IOException {
        return Files.readString(TOWN_PATH_FILE);
    }

    @Override
    public String importTowns() throws IOException {
        String json = readTownsFileContent();
        TownImportDTO[] townImportDTOS = this.gson.fromJson(json, TownImportDTO[].class);

        return Arrays.stream(townImportDTOS)
                .map(this::importValidTown)
                .collect(Collectors.joining(System.lineSeparator()));

    }

    private String importValidTown(TownImportDTO townImportDTO) {
        boolean isValid = this.validationUtil.isValid(townImportDTO);

        if (isValid) {
            Optional<Town> optTown = this.townRepository.findByName(townImportDTO.getName());

            if (optTown.isEmpty()) {
                Town town = this.mapper.map(townImportDTO, Town.class);

                this.townRepository.save(town);

                return String.format("Successfully imported Town %s - %d", town.getName(), town.getPopulation());
            }
        }

        return "Invalid Town";
    }
}
