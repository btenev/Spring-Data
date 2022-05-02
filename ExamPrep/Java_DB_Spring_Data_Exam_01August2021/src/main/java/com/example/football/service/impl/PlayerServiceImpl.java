package com.example.football.service.impl;

import com.example.football.models.dto.ImportPlayerDTO;
import com.example.football.models.dto.PlayerSummaryDTO;
import com.example.football.models.entity.Player;
import com.example.football.models.entity.Stat;
import com.example.football.models.entity.Team;
import com.example.football.models.entity.Town;
import com.example.football.repository.PlayerRepository;
import com.example.football.repository.StatRepository;
import com.example.football.repository.TeamRepository;
import com.example.football.repository.TownRepository;
import com.example.football.service.PlayerService;
import com.example.football.util.ValidationUtil;
import com.example.football.util.XmlParser;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.xml.bind.JAXBException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PlayerServiceImpl implements PlayerService {
    private static final Path PLAYER_PATH_FILE
            = Path.of("src", "main", "resources", "files", "xml", "players.xml");

    private final PlayerRepository playerRepository;
    private final TownRepository townRepository;
    private final TeamRepository teamRepository;
    private final StatRepository statRepository;

    private final XmlParser xmlParser;
    private final ValidationUtil validationUtil;
    private final ModelMapper mapper;

    @Autowired
    public PlayerServiceImpl(PlayerRepository playerRepository, TownRepository townRepository,
                             TeamRepository teamRepository, StatRepository statRepository, XmlParser xmlParser,
                             ValidationUtil validationUtil, ModelMapper mapper) throws JAXBException {
        this.playerRepository = playerRepository;
        this.townRepository = townRepository;
        this.teamRepository = teamRepository;
        this.statRepository = statRepository;

        this.xmlParser = xmlParser;
        this.mapper = mapper;
        this.validationUtil = validationUtil;

    }

    @Override
    public boolean areImported() {
        return this.playerRepository.count() > 0;
    }

    @Override
    public String readPlayersFileContent() throws IOException {
        return Files.readString(PLAYER_PATH_FILE);
    }

    @Override
    public String importPlayers() throws IOException, JAXBException {

        ImportPlayerDTO importPlayerDTO = this.xmlParser.fromFile(PLAYER_PATH_FILE, ImportPlayerDTO.class);

        return importPlayerDTO
                .getPlayers()
                .stream()
                .map(this::importValidPlayer)
                .collect(Collectors.joining(System.lineSeparator()));

    }

    private String importValidPlayer(PlayerSummaryDTO dto) {
        boolean isValid = this.validationUtil.isValid(dto);

        if (isValid) {
            Optional<Player> optPlayer = this.playerRepository.findByEmail(dto.getEmail());

            if (optPlayer.isEmpty()) {
                Player player = this.mapper.map(dto, Player.class);

                Optional<Team> team = this.teamRepository.findByName(dto.getTeam().getName());
                Optional<Town> town = this.townRepository.findByName(dto.getTown().getName());
                Optional<Stat> stat = this.statRepository.findById(dto.getStat().getId());

                player.setTeam(team.get());
                player.setTown(town.get());
                player.setStat(stat.get());

                this.playerRepository.save(player);

                return String.format("Successfully imported Player %s %s - %s",
                        player.getFirstName(), player.getLastName(), player.getPosition()
                                .toString());
            }
        }

        return "Invalid Player";
    }

    @Override
    public String exportBestPlayers() {
        LocalDate after = LocalDate.of(1995, 1, 1);
        LocalDate before = LocalDate.of(2003, 1, 1);
        List<Player> players = this.playerRepository
                .findByBirthDateBetweenOrderByStatShootingDescStatPassingDescStatEnduranceDescLastNameAsc(after, before);


        return players
                .stream()
                .map(Player::toString)
                .collect(Collectors.joining(System.lineSeparator()));
    }
}
