package softuni.exam.service.impl;

import com.google.gson.Gson;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import softuni.exam.domain.dto.ImportPlayersDTO;
import softuni.exam.domain.entities.Picture;
import softuni.exam.domain.entities.Player;
import softuni.exam.domain.entities.Team;
import softuni.exam.repository.PictureRepository;
import softuni.exam.repository.PlayerRepository;
import softuni.exam.repository.TeamRepository;
import softuni.exam.service.PlayerService;
import softuni.exam.util.ValidatorUtil;

import javax.transaction.Transactional;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PlayerServiceImpl implements PlayerService {
    private final Path PLAYERS_FILE_PATH
            = Path.of("src","main", "resources", "files", "json", "players.json");

    private final PlayerRepository playerRepository;
    private final PictureRepository pictureRepository;
    private final TeamRepository teamRepository;

    private final Gson gson;
    private final ValidatorUtil validatorUtil;
    private final ModelMapper modelMapper;

    @Autowired
    public PlayerServiceImpl(PlayerRepository playerRepository, PictureRepository pictureRepository,
                             TeamRepository teamRepository, Gson gson, ValidatorUtil validatorUtil,
                             ModelMapper modelMapper) {
        this.playerRepository = playerRepository;
        this.pictureRepository = pictureRepository;
        this.teamRepository = teamRepository;


        this.gson = gson;
        this.validatorUtil = validatorUtil;
        this.modelMapper = modelMapper;
    }

    @Override
    public String importPlayers() throws IOException {
        ImportPlayersDTO[] importPlayersDTOS = this.gson.fromJson(readPlayersJsonFile(), ImportPlayersDTO[].class);

        return Arrays.stream(importPlayersDTOS)
                .map(this::importValidPlayers)
                .collect(Collectors.joining(System.lineSeparator()));
    }

    private String importValidPlayers(ImportPlayersDTO importPlayersDTO) {
        boolean isValid = this.validatorUtil.isValid(importPlayersDTO);

        if (isValid) {
            Optional<Picture> optionalPicture = this.pictureRepository.findByUrl(importPlayersDTO.getPicture().getUrl());
            Optional<Team> optionalTeam = this.teamRepository.findByName(importPlayersDTO.getTeam().getName());

            if (optionalPicture.isPresent() && optionalTeam.isPresent()) {
                Player player = this.modelMapper.map(importPlayersDTO, Player.class);

                player.setPicture(optionalPicture.get());
                player.setTeam(optionalTeam.get());

                this.playerRepository.save(player);

                return String.format("Successfully imported player: %s %s", player.getFirstName(), player.getLastName());
            }

        }
        return "Invalid player";
    }

    @Override
    public boolean areImported() {
        return this.playerRepository.count() > 0;
    }

    @Override
    public String readPlayersJsonFile() throws IOException {
        return Files.readString(PLAYERS_FILE_PATH);
    }

    @Override
    public String exportPlayersWhereSalaryBiggerThan() {
        BigDecimal salary = new BigDecimal(100000);

        List<Player> players = this.playerRepository.findAllBySalaryGreaterThanOrderBySalaryDesc(salary);


        return players
                .stream()
                .map(player -> String.format("Player name: %s %s" + System.lineSeparator() +
                                             "        Number: %d" + System.lineSeparator() +
                                             "        Salary: %.2f" + System.lineSeparator() +
                                             "        Team: %s",
                                                player.getFirstName(), player.getLastName(),
                                                player.getNumber(),
                                                player.getSalary(),
                                                player.getTeam().getName()))
                .collect(Collectors.joining(System.lineSeparator()));
    }

    @Transactional
    @Override
    public String exportPlayersInATeam() {
        Team team = this.teamRepository.findByNameOrderByPlayersId("North Hub");

        String teamName = String.format("Team: %s" + System.lineSeparator()
                , team.getName());

        return teamName + team
                .getPlayers()
                .stream()
                .map(Player::toString)
                .collect(Collectors.joining(System.lineSeparator()));
    }
}
