package softuni.exam.service.impl;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import softuni.exam.domain.dto.ImportTeamsRootDTO;
import softuni.exam.domain.dto.TeamSummaryDTO;
import softuni.exam.domain.entities.Picture;
import softuni.exam.domain.entities.Team;
import softuni.exam.repository.PictureRepository;
import softuni.exam.repository.TeamRepository;
import softuni.exam.service.TeamService;
import softuni.exam.util.ValidatorUtil;
import softuni.exam.util.XmlParser;

import javax.xml.bind.JAXBException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class TeamServiceImpl implements TeamService {
    private final Path TEAMS_FILE_PATH
            = Path.of("src", "main", "resources", "files", "xml", "teams.xml");

    private final TeamRepository teamRepository;
    private final PictureRepository pictureRepository;

    private final XmlParser xmlParser;
    private final ValidatorUtil validatorUtil;
    private final ModelMapper modelMapper;

    @Autowired
    public TeamServiceImpl(TeamRepository teamRepository, PictureRepository pictureRepository, XmlParser xmlParser, ValidatorUtil validatorUtil,
                           ModelMapper modelMapper) {
        this.teamRepository = teamRepository;
        this.pictureRepository = pictureRepository;
        this.xmlParser = xmlParser;
        this.validatorUtil = validatorUtil;
        this.modelMapper = modelMapper;
    }

    @Override
    public String importTeams() throws JAXBException, IOException {
        ImportTeamsRootDTO importTeamDTO = this.xmlParser.fileFrom(TEAMS_FILE_PATH, ImportTeamsRootDTO.class);

        return importTeamDTO
                .getTeams()
                .stream()
                .map(this::importValidTeams)
                .collect(Collectors.joining(System.lineSeparator()));
    }

    private String importValidTeams(TeamSummaryDTO teamSummaryDTO) {
        boolean isValid = this.validatorUtil.isValid(teamSummaryDTO);

        if (isValid) {
            Optional<Picture> optionalPicture = this.pictureRepository.findByUrl(teamSummaryDTO.getPicture().getUrl());

            if (optionalPicture.isPresent()) {
                Team team = this.modelMapper.map(teamSummaryDTO, Team.class);

                team.setPicture(optionalPicture.get());

                this.teamRepository.save(team);

                return String.format("Successfully imported - %s", team.getName());
            }
        }

        return "Invalid team";
    }

    @Override
    public boolean areImported() {
        return this.teamRepository.count() > 0;
    }

    @Override
    public String readTeamsXmlFile() throws IOException {
        return Files.readString(TEAMS_FILE_PATH);
    }
}
