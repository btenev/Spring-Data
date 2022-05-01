package exam.service.impl;

import exam.model.dto.town.TownRootDTO;
import exam.model.dto.town.TownSummaryDTO;
import exam.model.entities.Town;
import exam.repository.TownRepository;
import exam.service.TownService;
import exam.util.ValidationUtil;
import exam.util.XmlParser;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.xml.bind.JAXBException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.stream.Collectors;

@Service
public class TownServiceImpl implements TownService {
    private final static Path TOWN_PATH_FILE
            = Path.of("src", "main", "resources", "files", "xml", "towns.xml");

    private final TownRepository townRepository;
    private final XmlParser xmlParser;
    private final ValidationUtil validationUtil;
    private final ModelMapper mapper;

    @Autowired
    public TownServiceImpl(TownRepository townRepository, XmlParser xmlParser, ValidationUtil validationUtil, ModelMapper mapper) throws JAXBException {
        this.townRepository = townRepository;

        this.xmlParser = xmlParser;
        this.validationUtil = validationUtil;
        this.mapper = mapper;

    }

    @Override
    public boolean areImported() {
        return this.townRepository.count() > 0;
    }

    @Override
    public String readTownsFileContent() throws IOException {
        return Files.readString(TOWN_PATH_FILE);
    }

    @Override
    public String importTowns() throws JAXBException, IOException {
        TownRootDTO townRootDTO = this.xmlParser.fromFile(TOWN_PATH_FILE, TownRootDTO.class);

        return townRootDTO
                .getTowns()
                .stream()
                .map(this::importTown)
                .collect(Collectors.joining(System.lineSeparator()));
    }

    private String importTown(TownSummaryDTO dto) {
        boolean isValid = this.validationUtil.isValid(dto);

        if (isValid) {
            Town town = this.mapper.map(dto, Town.class);

            this.townRepository.save(town);

            return "Successfully imported Town " + town.getName();
        }

        return "Invalid town";
    }
}
