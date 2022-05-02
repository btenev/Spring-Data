package com.example.football.service.impl;

import com.example.football.models.dto.TeamImportDTO;
import com.example.football.models.entity.Team;
import com.example.football.models.entity.Town;
import com.example.football.repository.TeamRepository;
import com.example.football.repository.TownRepository;
import com.example.football.service.TeamService;
import com.example.football.util.ValidationUtil;
import com.google.gson.Gson;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class TeamServiceImpl implements TeamService {
    Path TEAM_PATH_FILE
            = Path.of("src", "main", "resources", "files", "json", "teams.json");

    private final TeamRepository teamRepository;
    private final Gson gson;
    private final ValidationUtil validationUtil;
    private final ModelMapper mapper;
    private final TownRepository townRepository;

    @Autowired
    public TeamServiceImpl(TeamRepository teamRepository, Gson gson, ValidationUtil validationUtil, ModelMapper mapper,
                           TownRepository townRepository) {
        this.teamRepository = teamRepository;
        this.townRepository = townRepository;

        this.validationUtil = validationUtil;
        this.gson = gson;
        this.mapper = mapper;

    }

    @Override
    public boolean areImported() {
        return this.teamRepository.count() > 0;
    }

    @Override
    public String readTeamsFileContent() throws IOException {

        return Files.readString(TEAM_PATH_FILE);
    }

    @Override
    public String importTeams() throws IOException {

        TeamImportDTO[] teamImportDTOS = this.gson.fromJson(readTeamsFileContent(), TeamImportDTO[].class);

        return Arrays.stream(teamImportDTOS)
                .map(this::importValidTeam)
                .collect(Collectors.joining(System.lineSeparator()));

    }

    private String importValidTeam(TeamImportDTO dto) {
        boolean isValid = this.validationUtil.isValid(dto);

        if (isValid) {
            Optional<Team> optTeam = this.teamRepository.findByName(dto.getName());

            if (optTeam.isEmpty()) {
                Team team = this.mapper.map(dto, Team.class);

                Optional<Town> town = this.townRepository.findByName(dto.getTownName());

                team.setTown(town.get());

                this.teamRepository.save(team);

                return String.format("Successfully imported Team %s - %d", team.getName(), team.getFanBase());
            }
        }

        return "Invalid Team";
    }
}