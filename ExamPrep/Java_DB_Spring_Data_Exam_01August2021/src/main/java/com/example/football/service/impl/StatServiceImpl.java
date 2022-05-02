package com.example.football.service.impl;

import com.example.football.models.dto.ImportStatDTO;
import com.example.football.models.dto.StatValues;
import com.example.football.models.entity.Stat;
import com.example.football.repository.StatRepository;
import com.example.football.service.StatService;
import com.example.football.util.ValidationUtil;
import com.example.football.util.XmlParser;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.xml.bind.JAXBException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class StatServiceImpl implements StatService {
    private static final Path STATS_PATH_FILE
            = Path.of("src", "main", "resources", "files", "xml", "stats.xml");

    private final StatRepository statRepository;
    private final XmlParser xmlParser;
    private final ValidationUtil validationUtil;
    private final ModelMapper mapper;

    @Autowired
    public StatServiceImpl(StatRepository statRepository, XmlParser xmlParser, ValidationUtil validationUtil,
                           ModelMapper mapper) throws JAXBException {
        this.statRepository = statRepository;

        this.xmlParser = xmlParser;
        this.validationUtil = validationUtil;
        this.mapper = mapper;

    }

    @Override
    public boolean areImported() {
        return this.statRepository.count() > 0;
    }

    @Override
    public String readStatsFileContent() throws IOException {

        return Files.readString(STATS_PATH_FILE);
    }

    @Override
    public String importStats() throws IOException, JAXBException {

        ImportStatDTO importStatDTO = (ImportStatDTO) this.xmlParser.fromFile(STATS_PATH_FILE, ImportStatDTO.class);

        return importStatDTO
                .getStats()
                .stream()
                .map(this::importValidStat)
                .collect(Collectors.joining(System.lineSeparator()));

    }

    private String importValidStat(StatValues statValues) {
        boolean isValid = this.validationUtil.isValid(statValues);

        if (isValid) {

            Optional<Stat> optStat = this.statRepository
                    .findByShootingAndPassingAndEndurance(statValues.getShooting(), statValues.getPassing(),
                            statValues.getEndurance());

            if (optStat.isEmpty()) {
                Stat stat = this.mapper.map(statValues, Stat.class);

                this.statRepository.save(stat);

                return String.format("Successfully imported Stat %.2f - %.2f - %.2f",
                        stat.getShooting(), stat.getPassing(), stat.getEndurance());
            }

        }
        return "Invalid Stat";
    }
}
